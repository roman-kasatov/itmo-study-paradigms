"use strict"

const variablePositions = { 'x': 0, 'y': 1, 'z': 2 }

function AbstractOperation(...values) {
    this.values = values
    this._diff = {}

    this.evaluate = function (...args) {
        return this.func(...this.values.map(v => v.evaluate(...args)))
    }

    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + this.sign
    }

    this.diff = function (name) {
        if (!(name in this._diff)) {
            this._diff[name] = this.diff_function(name, ...values)
        }
        return this._diff[name]
    }

    this.prefix = function () {
        return `(${this.sign} ${this.values.map(v => v.prefix()).join(" ")})`
    }

    this.postfix = function () {
        return `(${this.values.map(v => v.postfix()).join(" ")} ${this.sign})`
    }
}

function createOperation(func, sign, diff_function) {
    let constructor = function (...values) {
        AbstractOperation.call(this, ...values)
    }
    constructor.prototype.func = func
    constructor.prototype.sign = sign
    constructor.prototype.diff_function = diff_function

    return constructor
}

const Subtract = createOperation(
    (a, b) => a - b,
    "-",
    (name, a, b) => new Subtract(a.diff(name), b.diff(name))
)

const Add = createOperation(
    (a, b) => a + b,
    "+",
    (name, a, b) => new Add(a.diff(name), b.diff(name))
)

const Multiply = createOperation(
    (a, b) => a * b,
    "*",
    (name, a, b) => new Add(new Multiply(a.diff(name), b), new Multiply(b.diff(name), a))
)

const Divide = createOperation(
    (a, b) => a / b,
    "/",
    (name, a, b) => new Divide(
        new Subtract(
            new Multiply(a.diff(name), b),
            new Multiply(a, b.diff(name)),
        ),
        new Multiply(b, b)
    )
)


const createSumsqN = argNmb =>
    createOperation(
        (...values) => values.map(a => a * a).reduce((sum, a) => sum + a, 0),
        "sumsq" + argNmb,
        (name, ...values) => new Multiply(
            treeSum(values
                .map(a =>
                    new Multiply(
                        a.diff(name),
                        a
                    )
                )
            ),
            new Const(2)
        )
    )

function createDistanceN(argNmb) {
    let constructor = function(...values) {
        this.values = values
        this.evaluate = function (...args) {
            return Math.sqrt(
                treeSum(values.map(a => new Multiply(a, a)))
                .evaluate(...args)
            )
        }
        this.toString = function () {
            return this.values.map(v => v.toString()).join(" ") + " " + "distance" + argNmb
        }
        this.diff = function (name) {
            return new Divide(
                treeSum(this.values.map(a =>
                    new Multiply(
                        a.diff(name),
                        a
                    ))),
                this
            )
        }
    }
    return constructor
}

const Sumexp = createOperation(
    (...values) => values.map(a => Math.exp(a)).reduce((sum, a) => sum + a, 0),
    "sumexp",
    (name, ...values) => treeSum(values
        .map(a =>
            new Multiply(
                a.diff(name),
                new Sumexp(a)
            )
        ))
)

const LSE = createOperation(
    (...values) => Math.log(values.map(a => Math.exp(a)).reduce((sum, a) => sum + a, 0)),
    "lse",
    (name, ...values) => new Divide(
        new Sumexp(...values).diff(name),
        new Sumexp(...values)
    )
)

const Sumsq2 = createSumsqN(2)
const Sumsq3 = createSumsqN(3)
const Sumsq4 = createSumsqN(4)
const Sumsq5 = createSumsqN(5)

const Distance2 = createDistanceN(2)
const Distance3 = createDistanceN(3)
const Distance4 = createDistanceN(4)
const Distance5 = createDistanceN(5)

const Negate = createOperation(
    a => -a,
    "negate",
    (name, a) => new Negate(a.diff(name))
)

function Variable(name) {
    this.evaluate = (...args) => args[variablePositions[name]]
    this.toString = () => name
    this.diff = (dif_name) => dif_name === name ? const1 : const0
    this.prefix = () => name
    this.postfix = this.prefix
}

function Const(value) {
    this.evaluate = () => value
    this.toString = () => value.toString()
    this.diff = () => const0
    this.prefix = () => value.toString()
    this.postfix = this.prefix
}

const const0 = new Const(0)
const const1 = new Const(1)

function treeSum(values) {
    if (values.length === 1) {
        return values[0]
    }
    return new Add(treeSum(values.slice(0, values.length / 2)), treeSum(values.slice(values.length / 2)))
}

const operations = {
    'negate': [Negate, 1],
    '+': [Add, 2],
    '-': [Subtract, 2],
    '*': [Multiply, 2],
    '/': [Divide, 2],
    "sumsq2": [Sumsq2, 2],
    "sumsq3": [Sumsq3, 3],
    "sumsq4": [Sumsq4, 4],
    "sumsq5": [Sumsq5, 5],
    "distance2": [Distance2, 2],
    "distance3": [Distance3, 3],
    "distance4": [Distance4, 4],
    "distance5": [Distance5, 5],
    "sumexp": [Sumexp, -1],
    "lse": [LSE, -1]
}


function ParseError(message) {
    Error.call(this.message)
    this.message = message
    this.toString = () => this.name + ": " + this.message
}
ParseError.prototype = Object.create(Error.prototype)
ParseError.prototype.name = "ParseError"

function createParseError(name) {
    let constructor = function(message) {
        ParseError.call(this, message)
        this.name = name
    }
    constructor.prototype = Object.create(ParseError.prototype)
    constructor.prototype.name = name
    return constructor
}

const EmptyInputError = createParseError(
    "EmptyInputError"
)

const OperationSignPositionError = createParseError(
    "OperationSignPositionError"
)

const ParenthesisError = createParseError(
    "ParenthesisError"
)

const ArgumentsError = createParseError(
    "ArgumentsError"
)

const InappropriateElementError = createParseError(
    "InappropriateElementError"
)

const splitByParenthesis = str =>
    str.replace(/([()])/g, " $& ").trim().split(/\s+/)

const parsePrefixPostfix = isPrefix => str => {
    if (str === '') {
        throw new EmptyInputError('Input string was empty')
    }

    let prevBit = null
    const stack = []
    const openingParenthesisPositions = []
    const operationsStack = []
    const bits = splitByParenthesis(str)
    for (const bit of bits) {
        if (bit === '(') {
            openingParenthesisPositions.push(stack.length)
        } else if (bit in variablePositions) {
            stack.push(new Variable(bit))
        } else if (!isNaN(bit)) {
            stack.push(new Const(parseInt(bit)))
        } else if (bit in operations) {
            if (isPrefix && prevBit !== '(') {
                throw new OperationSignPositionError(`Expected '(' before operation sign ${bit} but found ${prevBit}`)
            }
            operationsStack.push(bit)
        } else if (bit === ')') {
            if (!isPrefix && !(prevBit in operations)) {
                throw new OperationSignPositionError(`Expected operation sign before ${bit} but found ${prevBit}`)
            }
            if (operationsStack.length === 0) {
                throw new OperationSignPositionError(`Expected an operation sign before )`);
            }
            let operationName = operationsStack.pop()
            let [Func, argNmb] = operations[operationName]
            if (openingParenthesisPositions.length === 0) {
                throw new ParenthesisError(`Expected opening parenthesis before )`)
            }
            let values = stack.splice(openingParenthesisPositions.pop())
            if (argNmb !== -1 && values.length !== argNmb) {
                throw new ArgumentsError(`Expected ${argNmb} arguments for ${operationName} but found ${values.length}`)
            }
            try {
                stack.push(new Func(...values))
            } catch (error) {
                throw new ArgumentsError(`Inappropriate arguments for ${operationName} \
                    which caused a following error: ${error.message}`)
            }
        } else {
            throw new InappropriateElementError(`Can't parse '${bit}'`)
        }
        prevBit = bit
    }
    if (openingParenthesisPositions.length !== 0) {
        throw new ParenthesisError(`Too many '('`)
    }
    if (operationsStack.length !== 0) {
        throw new ParseError(`An error occurred while parsing operation ${operationsStack.pop()}`)
    } else if (stack.length !== 1) {
        throw new ParseError(`Too many arguments or parenthesis near ${stack[0]}`)
    }
    return stack.pop()
}

const parsePostfix = parsePrefixPostfix(false)
const parsePrefix = parsePrefixPostfix(true)


let parse = (str) => {
    let stack = []
    for (const bit of str.trim().split(/\s+/)) {
        if (bit in operations) { // operation
            let [Func, argNmb] = operations[bit]
            stack.push(new Func(...stack.splice(-argNmb, argNmb)));
        } else if (bit in variablePositions) { // variable
            stack.push(new Variable(bit));
        } else { // value
            stack.push(new Const(parseInt(bit)));
        }
    }
    return stack.pop()
}
