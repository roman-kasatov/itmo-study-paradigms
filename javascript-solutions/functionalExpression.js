
const abstractOperation = (func) => (...values) => (...args) => func(...values.map(v => v(...args)))

const variablePositions = { 'x': 0, 'y': 1, 'z': 2 }

const cnst = value => () => value;
const variable = name => (...args) => args[variablePositions[name]]

const one = cnst(1);
const two = cnst(2);

const floor = abstractOperation(Math.floor)
const ceil = abstractOperation(Math.ceil)

const subtract = abstractOperation((a, b) => a - b)
const add = abstractOperation((a, b) => a + b)
const multiply = abstractOperation((a, b) => a * b)
const divide = abstractOperation((a, b) => a / b)
const negate = abstractOperation(a => -a)

const madd = abstractOperation((a, b, c) => a * b + c)

const operations = {
  'one': [() => one, 0],
  'two': [() => two, 0],
  'negate': [negate, 1],
  '_': [floor, 1],
  '^': [ceil, 1],
  '+': [add, 2],
  '-': [subtract, 2],
  '*': [multiply, 2],
  '/': [divide, 2],
  '*+': [madd, 3]
}

let parse = (str) => {
  let stack = []
  for (const bit of str.split(" ").filter(s => s !== '')) {
    if (bit in operations) { // operation
      let [func, argNmb] = operations[bit]
      stack.push(func(...stack.splice(-argNmb, argNmb)));
    } else if (bit in variablePositions) { // variable
      stack.push(variable(bit));
    } else { // value
      stack.push(cnst(parseInt(bit)));
    }
  }
  return stack.pop()
}
