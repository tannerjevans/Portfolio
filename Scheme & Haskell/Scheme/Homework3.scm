(define name '("Evans" "Tanner"))
(define netID "tannerjevans@unm.edu")


;; === Part 1, Points: 10, Weight: 1/3 ===
(define compose
    (lambda (f g)
        (lambda (x)
            (f (g x))
        )
    )
)

(define id (lambda (x) x))

;; problem1-1-7.2    p234
;; Points: 1/10
;; ((compose3 id id id) 4) => 4
(define compose3
  (lambda (f g h)
    (lambda (x)
      ((compose f (compose g h)) x)))
)


;; problem1-1-7.3    p234
;; Points: 1/10
;; ((compose-many id id id id) 4) => 4
(define compose-many
  (lambda args
    (lambda (x)
      ((if (null? args)
	  (lambda (x) x)
	  (compose (car args) (apply compose-many (cdr args)))) x)))
  )

;; problem1-1-7.6    p235
;; Points: 1/10
;; (map-first-two + '(2 3 4 5 7)) => '(5 7 9 12)
(define map-first-two
  (lambda (proc ls)
    (if (null? (cdr ls))
	'()
	(cons (proc (car ls) (cadr ls))
	      (map-first-two proc (cdr ls)))))
)


;; problem1-1-7.7    p235
;; Points: 1/10
;; (reduce + '(2 3 4 5 7)) => 21
(define reduce
  (lambda (proc ls)
    (if (or (null? ls)
	    (null? (cdr ls)))
	(display "Error: reduce requires at least two arguments in its list.")
	(letrec
	    ((loop
	      (lambda (ls)
		(if (null? (cdr ls))
		    (car ls)
		    (loop (cons (proc (car ls) (cadr ls)) (cddr ls)))))))
	  (loop ls))))
)


;; problem1-1-7.8    p236
;; Points: 1/10
;; (andmap positive? '(3 4 6 9)) => #t
(define andmap
  (lambda (pred ls)
    (cond
     ((null? ls)
      #t)
     ((pred (car ls))
      (andmap pred (cdr ls)))
     (else
      #f)))
)


;; problem1-1-7.12-a    p243
;; Points: 0.5/10
;; ((curried* 5) 25) => 125
(define curried*
  (lambda (m)
    (lambda (n)
      (* m n)))
)

;; problem1-1-7.12-b    p243
;; Points: 0.5/10
;; (times10 580) => 5800
(define times10
    (curried* 10)
)


;; problem1-1-7.18-a    p244
;; Points: 0.5/10
;; (between? 4 5 6) => #t
(define between?
  (lambda (x y z)
    (and (< x y)
	 (< y z)))
)
;; problem1-1-7.18-b    p244
;; Points: 0.5/10
;; (((between?-c 4) 5) 6) => #t
(define between?-c
  (lambda (x)
    (lambda (y)
      (lambda (z)
	(between? x y z))))
)


(define flat-recur
  (lambda (seed list-proc)
    (letrec
	((helper
	  (lambda (ls)
	    (if (null? ls)
		seed
		(list-proc (car ls) (helper (cdr ls)))))))
      helper)))

;; problem1-1-7.22    p250
;; Points: 1/10
;; ((mult-by-scalar 3) '(1 -2 3 -4)) => '(3 -6 9 -12)
(define mult-by-scalar
  (lambda (c)
    (lambda (ntpl)
      ((flat-recur '() (lambda (x y) (cons (* c x) y)))
       ntpl)))
)

(define deep-recur
  (lambda (seed item-proc list-proc)
    (letrec
      ((helper
        (lambda (ls)
          (if (null? ls)
              seed
              (let ((a (car ls)))
                (if (or (pair? a) (null? a))
                    (list-proc (helper a) (helper (cdr ls)))
                    (item-proc a (helper (cdr ls)))))))))
      helper)))


;; problem1-1-7.30    p
;; Points: 1/10
;; (reverse-all '(1 (2 3) 4)) => '(4 (3 2) 1)
(define reverse-all
  (lambda (ls)
    (let ((snoc (lambda (x y) (append y (list x)))))
      ((deep-recur '() snoc snoc) ls)))
)


;; problem1-1-7.31    p
;; Points: 1/10
;; ((flat-recur 0 +) '(1 2 3)) => 6
(define flat-recur
  (lambda (seed proc)
    (deep-recur seed proc proc))
)


;; === Part 2, Points: 10, Weight: 1/3 ===

;; problem2-1-a
;; Points: 1/8
;; ((tail-recur zero? sub1 * 1) 5) => 120
(define tail-recur
  (lambda (bpred xproc aproc acc0)
    (lambda (x)
      (letrec
	  ((loop
	    (lambda (x acc)
	      (if (bpred x)
		  acc
		  (loop (xproc x) (aproc x acc))))))
	(loop x acc0))))
)

;; problem2-1-b
;; Points: 1/8
;; (reverse '(1 2 3)) => '(3 2 1)
(define reverse
  (tail-recur null? cdr (lambda (x acc) (cons (car x) acc)) '())
)

;; problem2-1-c
;; Points: 1/8
;; (iota 3) => '(1 2 3)
(define iota
  (tail-recur zero? sub1 cons '())
)

;; problem2-2-
;; Points: 1/8
;; ((disjunction2 symbol? procedure?) +) => #t
(define disjunction2
  (lambda (pred1 pred2)
    (lambda (item)
      (or (pred1 item)
	  (pred2 item))))
)

;; problem2-3-
;; Points: 1/8
;; ((disjunction procedure?) +) => #t
(define disjunction
  (lambda args
    (lambda (item)
      (letrec
	  ((loop
	    (lambda (ls)
	      (if (null? ls)
		  #f
		  (or (eval (list (car ls) item))
		      (loop (cdr ls)))))))
	(loop args))))
  )


;; problem2-4-
;; Points: 1/8
;; (matrix-map even? '((1 2) (3 4))) => '((#f #t) (#f #t))
(define matrix-map
  (lambda (f matrix)
      (map (lambda (ls) (map f ls)) matrix))
)

;; problem2-5
(define fold
  (lambda (seed proc)
    (letrec
        ((pattern
	  (lambda (ls)
	    (if (null? ls)
		seed
		(proc (car ls)
		      (pattern (cdr ls)))))))
      pattern)))


;; problem2-5-a
;; Points: 1/8
;; (delete-duplicates '(a b a b a b a b)) => '(a b)
(define delete-duplicates
    (fold '() (lambda (x y) (cons x (remove x y))))
)

;; problem2-5-b
;; Points: 1/8
;; (assoc 'b '((a 1) (b 2))) => '(b 2)
(define assoc
  (lambda (item ls)
    ((fold #f (lambda (x y) (if (eq? item (car x)) x y)))
     ls))
)


;; === Part 3, Points: 8, Weight: 1/3 ===

;; apply, select, map, filter, outer-product, iota

;; I have defined here those of these functions which
;; are not provided by Scheme.

(define select
  (lambda (pred)
    (lambda (ls0 ls1)
      (map cdr
	   (filter
	    (lambda (x) (pred (car x)))
	    (map cons ls0 ls1))))))

(define outer-product
  (lambda (proc)
    (lambda (us vs)
      (map (lambda (u)
	(map (lambda (v) (proc u v)) vs)) us))))

(define iota
  (lambda (n)
    (letrec
	((loop
	  (lambda (n acc)
	    (if (= n 0)
		acc
		(loop (sub1 n)
		      (cons n acc))))))
      (loop n '()))))


;; problem3-1-
;; Points: 1/8
;; (length '(1 2 3 4)) => 4
(define length
  (lambda (ls)
    (apply + (map (lambda (x) 1) ls)))
)

;; problem3-2-
;; Points: 1/8
;; (sum-of-squares 3 4 5 6) => 86
(define sum-of-squares
  (lambda args
    (apply + (map sqr args)))
)

;; problem3-3-
;; Points: 1/8
;; (avg 4 5 5 5 5 5) => 29/6
(define avg
  (lambda args
    (/ (apply + args)
       (length args)))
)

;; problem3-4-
;; Points: 1/8
;; (avg-odd 4 5 5 5 5 5) => 5
(define avg-odd
  (lambda args
    (apply avg (filter odd? args)))
)

;; problem3-5-
;; Points: 1/8
;; (shortest '(1 2) '(2 3 4) '(4) '(5 6 7 8)) => '(4)
(define shortest
  (lambda args
    (let* ((lengths (map length args))
	   (minLength (apply min lengths)))
      (car ((select (lambda (x) (= minLength x))) lengths args))))
  )

;; problem3-6-
;; Points: 1/8
;; (avg-fact 2 3 4) => 32/3
(define avg-fact
  (lambda args
    (apply avg (map (lambda (ls) (apply * ls)) (map iota args))))
)

;; problem3-7-
;; Points: 1/8
;; ((tally even?) '(1 2 3 4 5)) => 2
(define tally
  (lambda (pred)
    (lambda (ls)
      (length (filter pred ls))))
)

;; problem3-8-
;; Points: 1/8
;; (list-ref '(5 7 60 98) 0) => 5
(define list-ref
  (lambda (ls int)
    (let ((indices (map sub1 (iota (length ls)))))
      (car ((select (lambda (x) (= x int))) indices ls))))
)
