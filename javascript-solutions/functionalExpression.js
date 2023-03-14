
const abstractOperation = (func) => (...values) => (...args) => func(...values.map(v => v(...args)))

// const abstractBinaryOperation = (func) => abstractOperation( fub//(func) => (a, b) => (...args) => func(a(...args),  b(...args))
// const abstractUnaryOperation = (func) => (a) => (...args) => func(a(...args))

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



const split = (str) => {
  const res = [];
  let startPos = 0;
  for (let i = 0; i < str.length; i++) {
    if (str[i] == ' ') {
      if (startPos < i) {
        res.push(str.substring(startPos, i));
      }
      startPos = i + 1;
    }
  }
  if (startPos < str.length) {
    res.push(str.substring(startPos));
  }
  return res;
}

const retrieve = (arr, cnt) => { 
  let res = [];
  while (cnt-- > 0) {
    res.push(arr.pop())
  }
  return res.reverse()
}

let parse = (str) => {
  let stack = []

  for (const bit of split(str)) {
    if (bit in operations) { // operation
      let [func, argNmb] = operations[bit]
      let args = retrieve(stack, argNmb);
      stack.push(func(...args));
    } else if (bit in variablePositions) { // variable
      stack.push(variable(bit));
    } else { // value
      stack.push(cnst(parseInt(bit)));
    }
  }
  return stack.pop()
}
            
