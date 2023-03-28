
const variablePositions = { 'x': 0, 'y': 1, 'z': 2 }

function AbstractOperation(...values) {
    this.values = values

    this.evaluate = function (...args) {
        return this.func(...this.values.map(v => v.evaluate(...args)))
    }

    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + this.sign
    }

    this.diff = function (name) {
        return this.diff_function(name, ...values)
    }

    this.prefix = function () {
        return `(${this.sign} ${this.values.map(v => v.prefix()).join(" ")})`
    }
}

function createOperation(func, sign, diff_function) {
    constructor = function (...values) {
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
        (name, ...values) => values
            .map(a =>
                new Multiply(
                    new Multiply(
                        a.diff(name),
                        new Const(2)
                    ),
                    a
                )
            )
            .reduce((sum, a) => new Add(sum, a), new Const(0))
    )

function createDistanceN(argNmb) {
    constructor = function(...values) {
        this.values = values
        this.evaluate = function (...args) {
            return Math.sqrt(values.map(a => new Multiply(a, a))
                .reduce((sum, a) => new Add(sum, a), new Const(0))
                .evaluate(...args))
        }
        this.toString = function () {
            return this.values.map(v => v.toString()).join(" ") + " " + "distance" + argNmb
        }
        this.diff = function (name) {
            return new Divide(
                this.values.map(a =>
                    new Multiply(
                        a.diff(name),
                        a
                    ))
                    .reduce((sum, a) => new Add(sum, a), new Const(0)),
                this
            )
        }
    }
    return constructor
}

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
    this.diff = (dif_name) => dif_name === name ? new Const(1) : new Const(0)
    this.prefix = () => name
}

function Const(value) {
    this.evaluate = () => value
    this.toString = () => value.toString()
    this.diff = () => new Const(0)
    this.prefix = () => value.toString()
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
    "distance5": [Distance5, 5]
}


function ParseError(message) {
    this.prototype = Error.prototype
    this.message = message
}

function parsePrefix(str) {
    const stack = []
    const operationsStack = []
    const bits = str.split(/([()])/g).reduce((res, cur) => {
        const withoutSpaces = cur.match(/[^\s]+/g);
        if (withoutSpaces !== null) {
            res.push(...withoutSpaces)
        }
        return res
    }, [])
    for (const bit of bits) {
        if (bit === '(') {
            stack.push(bit)
        } else if (bit in variablePositions) {
            stack.push(new Variable(bit))
        } else if (!isNaN(bit)) {
            stack.push(new Const(parseInt(bit)))
        } else if (bit in operations) {
            operationsStack.push(bit)
        } else if (bit === ')') {
            let operationName = operationsStack.pop()
            let [Func, argNmb] = operations[operationName]
            if (stack.length <= argNmb) {
                throw new ParseError(`Not enough arguments for operation ${operationName}`)
            }
            let values = stack.splice(-argNmb)
            for (const value of values) {
                if (value === '(') {
                    throw new ParseError(`Not enough arguments for operation ${operationName}`)
                }
            }
            if (stack.pop() !== '(') {
                throw new ParseError(`Too many arguments for operation ${operationName}`)
            }
            stack.push(new Func(...values))
        } else {
            throw new ParseError(`Can't parse '${bit}'`)
        }
    }
    if (operationsStack.length !== 0) {
        throw new ParseError(`An error occurred while parsing operation ${operationsStack.pop()}`)
    } else if (stack.length !== 1) {
        throw new ParseError(`Too many arguments or parenthesis near ${stack[0]}`)
    }
    return stack.pop()
}

let parse = (str) => {
    let stack = []
    for (const bit of str.split(" ").filter(s => s !== '')) {
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

