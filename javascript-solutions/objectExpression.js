
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


function Sumsq2(...values) {
    this.values = values
    this.evaluate = function (...args) {
        return this.values.map(a => new Multiply(a, a))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
            .evaluate(...args)
    }
    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + "sumsq2"
    }
    this.diff = function (name) {
        return this.values.map(a => new Multiply(a, new Const(2)))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
    }
}

function Sumsq3(...values) {
    this.values = values
    this.evaluate = function (...args) {
        return values.map(a => new Multiply(a, a))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
            .evaluate(...args)
    }
    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + "sumsq3"
    }
    this.diff = function (name) {
        return this.values.map(a => new Multiply(a, new Const(2)))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
    }
}

function Sumsq4(...values) {
    this.values = values
    this.evaluate = function (...args) {
        return values.map(a => new Multiply(a, a))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
            .evaluate(...args)
    }
    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + "sumsq4"
    }
    this.diff = function (name) {
        return this.values.map(a => new Multiply(a, new Const(2)))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
    }
}

function Sumsq5(...values) {
    this.values = values
    this.evaluate = function (...args) {
        return values.map(a => new Multiply(a, a))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
            .evaluate(...args)
    }
    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + "sumsq5"
    }
    this.diff = function (name) {
        return this.values.map(a => new Multiply(a, new Const(2)))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
    }
}

function Distance2(...values) {
    this.values = values
    this.evaluate = function (...args) {
        return Math.sqrt(values.map(a => new Multiply(a, a))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
            .evaluate(...args))
    }
    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + "distance2"
    }
}

function Distance3(...values) {
    this.values = values
    this.evaluate = function (...args) {
        return Math.sqrt(values.map(a => new Multiply(a, a))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
            .evaluate(...args))
    }
    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + "distance3"
    }
}

function Distance4(...values) {
    this.values = values
    this.evaluate = function (...args) {
        return Math.sqrt(values.map(a => new Multiply(a, a))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
            .evaluate(...args))
    }
    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + "distance4"
    }
}

function Distance5(...values) {
    this.values = values
    this.evaluate = function (...args) {
        return Math.sqrt(values.map(a => new Multiply(a, a))
            .reduce((sum, a) => new Add(sum, a), new Const(0))
            .evaluate(...args))
    }
    this.toString = function () {
        return this.values.map(v => v.toString()).join(" ") + " " + "distance5"
    }
}


const Negate = createOperation(
    a => -a,
    "negate",
    (name, a) => new Negate(a.diff(name))
)

function Variable(name) {
    this.evaluate = (...args) => args[variablePositions[name]]
    this.toString = () => name
    this.diff = (dif_name) => dif_name === name ? new Const(1) : new Const(0)
}

function Const(value) {
    this.evaluate = () => value
    this.toString = () => value.toString()
    this.diff = () => new Const(0)
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

/*
Testing: new Sumsq2(new Const(2), new Const(3)).diff('x')
d = 13.000000
Exception in thread "main" java.lang.AssertionError:
    in expr.evaluate(2.00000000000000000000,2.00000000000000000000,2.00000000000000000000);
    where expr = new Sumsq2(new Const(2), new Const(3)).diff('x')
: Expected 0.000000000000, found 10.000000000000
        at base.Asserts.error(Unknown Source)
        at base.Asserts.assertTrue(Unknown Source)
        at base.Selector.lambda$test$2(Unknown Source)
        at base.Log.lambda$action$0(Unknown Source)
        at base.Log.silentScope(Unknown Source)
        at base.Log.scope(Unknown Source)
        at base.Log.scope(Unknown Source)
        at base.Selector.lambda$test$3(Unknown Source)
        at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        at base.Selector.test(Unknown Source)
        at base.Selector.main(Unknown Source)
        at jstest.object.FullObjectTest.main(Unknown Source)
ERROR: Tests: failed

 */