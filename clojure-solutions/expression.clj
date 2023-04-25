
(defn operation [func]
  (fn [& operands]
    (fn [values]
      (apply func
             (cond
               (= 0 (count operands)) []
               :else ((apply juxt operands) values))
             )
      )
    )
  )

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation
             (fn [& args]
               (cond
                 (= 1 (count args)) (/ 1 (double (first args)))
                 :else (/ (double (first args)) (double (apply * (rest args))))
                 )
               )
             )
  )

(def sumexp (operation
             (fn [& args]
               (apply + (map #(. Math exp %) args))
               )
             )
  )

(def lse (operation
             (fn [& args]
               (Math/log (apply + (map #(. Math exp %) args)))
               )
             )
  )

(def negate subtract)

(defn constant [value]
  (fn [values] value)
  )

(defn variable [name]
  (fn [values]
    (get values name)
    )
  )

(def operations
  {
    '+ add
    '- subtract
    '* multiply
    '/ divide
    'negate negate
    'sumexp sumexp
    'lse lse
    }
  )

(defn parseFunction [string]
  (letfn
    [(inner [expression]
            (cond
              (number? expression) (constant expression)
              (symbol? expression) (variable (str expression))
              :else (apply (get operations (first expression)) (map inner (rest expression)))
              )
            )]
    (inner (read-string string))
    )
  )