(ns clj.Bridge
  (:use fungp.core) ;; include the core framework
  (:use fungp.util) ;; include utility functions
  (:use clojure.pprint)
  (:use clojure.walk)
  (:gen-class
    :methods [#^{:static true} [greet [] void]
              #^{:static true} [tutorial [int int] void]]))

(defn -greet []
  (println "Hello world from Clojure!")
  (let [a (list 0 1 2 3 4)
        ftext '(let [a (int-array 'aplaceholder)] (list a (fn [i] (aget a i))))
        ftext2 (postwalk #(if (= % 'aplaceholder) a (do %)) ftext)]
    (println ftext2)
    (let [af (eval ftext2)
          a (nth af 0)
          f (nth af 1)]
      (println (f 2))
      (aset a 2 10)
      (println (f 2)))))

(def sample-functions
  "Here's a vector of vectors consisting of [symbol arity] pairs. The symbol must resolve
   to a function, and the arity number is an integer representing how many arguments
   that function takes."
  '[[+ 2][- 2][* 2][fungp.util/abs 1]
    [fungp.util/sdiv 2][inc 1][dec 1]])

(def sample-parameters
  "This defines the parameters (or, in this case, parameter) to be used to eval the
  generated functions."
  ['i 'p1 'p2 'p3 'p4])

(def number-literals
  "This generates floating point numbers up to 10 as number literals to be used in the code."
  (map float (range 10)))

(def training-range
  "This defines the range of input to use as training input. The first argument for map here uses a shortcut for
   single-variable anonymous functions in Clojure."
  (map #(* 2 (- % 5)) (range 10)))

(defn match-func
  "For sake of convenience, we can define a function to generate the outputs we're attempting to match."
  [a] (abs (* 3 (* a a))))

(def actual-output
  "This defines the actual outputs we are trying to match."
  (map float (map match-func training-range)))

;; (eval (list 'fn parameters tree))

(defn sample-fitness
  "The fitness function; it takes a tree, evals it, and returns a fitness/error score."
  [tree]
  (try
    (let [f (compile-tree tree sample-parameters) ;; compile using compile-tree
          results (map f training-range)] ;; map the function to the test range
      ;; then we compare the test results to the actual expected output
      ;; off-by-sq is a utility function that calculates difference squared
      (reduce + (map off-by-sq actual-output results)))
    ;; not necessary here, but this is how you might catch a possible exception
    (catch Exception e (println e) (println tree))))

(defn sample-report
  "Reporting function. Prints out the tree and its score"
  [tree fitness]
  (pprint tree)
  (println (str "Error:\t" fitness "\n"))
  (flush))

(defn test-genetic-program
  "This is the function that launches *fungp* and starts the evolution. It takes iteration and migration counts as parameters."
  [n1 n2]
  (println "\nfungp :: Functional Genetic Programming in Clojure")
  (println "Mike Vollmer, 2012")
  (println (str "Test inputs: " (vec training-range)))
  (println (str "Test outputs: " (vec actual-output)))
  (println (str "Max generations: " (* n1 n2)))
  (println)
  ;; These keyword arguments specify the options for fungp. They should be self-explanatory,
  ;; but you can read more about them in fungp.core
  (let [options {:iterations n1 :migrations n2 :num-islands 6 :population-size 40
                 :tournament-size 5 :mutation-probability 0.1
                 :max-depth 10 :terminals sample-parameters
                 :numbers number-literals :fitness sample-fitness
                 :functions sample-functions :report sample-report }
        ;; the data returned by run-genetic-programming is as follows:
        ;; [population [best-tree score]]
        ;; since we don't care about keeping the whole population
        ;; around, we can save off the tree and score like this
        [tree score] (rest (run-genetic-programming options))]
    ;; that's it!
    (do (println "Done!")
        (sample-report tree score))))

(defn -tutorial [n1 n2]
  (test-genetic-program n1 n2)
  (println "Return to Scala"))