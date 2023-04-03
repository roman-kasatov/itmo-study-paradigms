Тесты для ДЗ-8 не прошли
```
Exception in thread "main" java.lang.AssertionError: Error while testing new Divide(new Negate(new Variable('x')), new Const(2)): 
    in expr.postfix()
    where expr = new Divide(new Negate(new Variable('x')), new Const(2))
:
     expected `((x negate) 2 /)`,
       actual `((negate x) 2 /)`

```