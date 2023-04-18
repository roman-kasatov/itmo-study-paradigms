
(defn check-vector [v] (and (vector? v) (every? number? v)))
(defn check-equal-size [& vs] (or (= 0 (count vs)) (apply = (map count vs))))
(defn check-matrix [m] (and (vector? m) (every? vector? m) (apply check-equal-size m)))
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
  (reduce + 0 (apply v* vs))
  )

(defn v*s [v & ss]
  {:pre [(check-vector v) (every? number? ss)]
   :post [(check-vector %) (check-equal-size % v)]}
  (mapv (partial * (apply * ss)) v)
  )

(defn vect [& vs]
  {:pre [(every? check-vector vs) (apply check-equal-size vs)]
   :post [(check-vector %) (check-equal-size % (first vs))]}
  (reduce
   (fn [v1 v2]
     (vector
      (- (* (nth v1 1) (nth v2 2)) (* (nth v1 2) (nth v2 1)))
      (- (* (nth v1 2) (nth v2 0)) (* (nth v1 0) (nth v2 2)))
      (- (* (nth v1 0) (nth v2 1)) (* (nth v1 1) (nth v2 0)))
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
;      :post [(check-matrix %) (check-equal-size-matrices % (first ms))]

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
  (reduce (fn [m1 m2] (mapv (fn [row] (m*v (transpose m2) row)) m1)) ms)
  )

(defn tensorwise [operation]
  (letfn [(func [& ts]
                {:pre [(or
                        (every? number? ts)
                        (and (every? vector? ts) (apply check-equal-size ts))
                        )]}
                (cond
                  (every? number? ts) (apply operation ts)
                  :else (apply mapv func ts)
                  )
                )]
    func
    )
  )

(def t+ (tensorwise +))
(def t- (tensorwise -))
(def t* (tensorwise *))
(def td (tensorwise /))


