
(defn check-vector [v] (and (vector? v) (every? number? v)))
(defn check-equal-size [& vs] (or (= 0 (count vs)) (apply = (map count vs))))
(defn check-matrix [m] (and (vector? m) (every? check-vector m) (apply check-equal-size m)))
(defn check-equal-size-matrices [& ms]
  (and
   (apply check-equal-size ms)
   (apply check-equal-size (map first ms))
   )
  )


(defn elementwise [operation]
   (fn [& vs]
     {:pre [(every? check-vector vs) (apply check-equal-size vs)]
      :post [(check-vector %) (check-equal-size % (first vs))]}
     (apply mapv operation vs))
  )

(def v+ (elementwise +))
(def v- (elementwise -))
(def v* (elementwise *))
(def vd (elementwise /))

(defn scalar [& vs]
  {:pre [(every? check-vector vs) (apply check-equal-size vs)]
   :post [(number? %)]}
  (apply + (apply v* vs))
  )

(defn v*s [v & ss]
  {:pre [(check-vector v) (every? number? ss)]
   :post [(check-vector %) (check-equal-size % v)]}
  (mapv (partial * (apply * ss)) v)
  )

(defn vect [& vs]
  {:pre [(every? check-vector vs) (apply check-equal-size vs) (= 3 (count (first vs)))]
   :post [(check-vector %) (check-equal-size % (first vs))]}
  (reduce
   (fn [[v1x v1y v1z] [v2x v2y v2z]]
     (vector
      (- (* v1y v2z) (* v1z v2y))
      (- (* v1z v2x) (* v1x v2z))
      (- (* v1x v2y) (* v1y v2x))
      )
     )
   vs
   )
  )

(defn vectorwise [operation]
  (fn [& ms]
    {:pre [(every? check-matrix ms) (apply check-equal-size-matrices ms)]}
    (apply mapv operation ms))
  )


(def m+ (vectorwise v+))
(def m- (vectorwise v-))
(def m* (vectorwise v*))
(def md (vectorwise vd))

(defn m*s [m & ss]
  {:pre [(check-matrix m) (every? number? ss)]
   :post [(check-matrix %) (check-equal-size-matrices % m)]}
  ((vectorwise (fn [v] (v*s v (apply * ss)))) m))

(defn m*v [m v]
  {:pre [(check-matrix m) (check-vector v) (check-equal-size (first m) v)]
   :post [(check-vector %) (check-equal-size % m)]
   }
  ((vectorwise (fn [row] (scalar row v))) m)
  )

(defn transpose [m]
  {:pre [(check-matrix m)]
   :post [(check-matrix %)]
   }
  (apply mapv vector m)
  )

(defn m*m [& ms]
  {:pre [(every? check-matrix ms)]
   :post []
   }
    (reduce
     (fn [m1 m2]
       (let [transposed-m2 (transpose m2)]
        (mapv (fn [row] (m*v transposed-m2 row)) m1)
         )
       )
     ms
     )
  )

(defn check-tensor [t]
  (or
   (number? t)
   (every? number? t)
   (and
    (apply check-equal-size t)
    ; (t+ [[[1, 2], [3, 4]], [[5], [6]]]    [[[1, 2], [3, 4]], [[5], [6]]])
    (check-tensor (apply concat t))
    )
   )
  )

(defn tensorwise [operation]
  (letfn
    [(func [& ts]
           {:pre [(every? check-tensor ts)]}
           (letfn
             [(recursion [& ts]
                         {:pre [(or
                                 (every? number? ts)
                                 (apply check-equal-size ts)
                                 )]}
                         (cond
                           (every? number? ts) (apply operation ts)
                           :else (apply mapv recursion ts)
                           )
                         )]
             (apply recursion ts)
             )
           )]
    func
    )
  )

(def t+ (tensorwise +))
(def t- (tensorwise -))
(def t* (tensorwise *))
(def td (tensorwise /))
