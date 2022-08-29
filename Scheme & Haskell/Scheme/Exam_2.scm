
;; Fill in your name and net ID below
;;  replacing Last with your last name(s)
;;  replacing First with your first name(s)
;;  and replacing netID@unm.edu with your unm email address
(define name '("Evans" "Tanner"))
(define netID "tannerjevans@unm.edu")

;; If you accept the academic honesty pledge, replace the "" below with
;;      "I accept and agree to the academic honesty pledge"
(define academic-honesty-pledge
    "I accept and agree to the academic honesty pledge")


;; Fill out the following defintions in wichever order you choose
;;  your final submission should evaluate without errors.
;; Each defintion comes pre-filled as '(), delete the '() prior to
;;  defining your function. Do not replace it with another define:
;;  i.e. The following defintion is WRONG:
;;      (define func
;;          (define func
;;              (lambda ...
;;
;; Do NOT submit your answer as a quoted list, i.e. the following 
;;  definition is also WRONG, as the quote was not removed:
;;      (define func
;;          '(lambda ...


;; ======= Variadic Functions =======

;; Using the instructions provided on LEARN, give a defintion for number
(define number
  (lambda args
    (if (null? args)
	0
	(+ 1
	   (apply number (cdr args)))))
)


;; ============ Currying ============

;; Using the instructions provided on LEARN, give a defintion for sum-of-squares-c
(define sum-of-squares-c
  (lambda (a)
    (lambda (b)
      (lambda (c)
	(lambda (d)
	  (+ (* a a)
	     (* b b)
	     (* c c)
	     (* d d))))))
)

(define sum-of-squares
  (lambda (a b c d)
    (+ (* a a)
       (* b b)
       (* c c)
       (* d d))))

;; Using the instructions provided on LEARN, give a defintion for curry4
(define curry4
  (lambda (proc)
    (lambda (a)
      (lambda (b)
	(lambda (c)
	  (lambda (d)
	    (proc a b c d))))))
)


;; ============== Fold ==============

;; You may use the definition of fold provided
(define fold
  (lambda (proc seed)
    (letrec
	((pattern
	  (lambda (ls)
	    (if (null? ls)
		seed
		(proc (car ls) (pattern (cdr ls)))))))
      pattern)))

;; Using the instructions provided on LEARN, give a defintion for snoc
(define snoc
  (lambda (ls x)
    ((fold cons (list x)) ls))
)

;; Using the instructions provided on LEARN, give a defintion for take-while
(define take-while
  (lambda (proc ls)
    ((fold (lambda (x y) (if (proc x) (cons x y) '())) '()) ls))
)


;; ===== Procedural Abstraction =====

;; Using the instructions provided on LEARN, give a defintion for fold-tree
(define fold-tree
  (lambda (proc1 seed proc2)
    (letrec
	((pattern
	  (lambda (sexpr) 
	    (cond
	     ((null? sexpr) seed)
	     ((pair? sexpr)
	      (proc1 (pattern (car sexpr))
		     (pattern (cdr sexpr))))
	     (else
	      (proc2 sexpr))))))
      pattern))
)

;; Using the instructions provided on LEARN, give a defintion for depth
(define depth
  (lambda (sexpr)
    ((fold-tree (lambda (x y) (add1 (max x y))) 0 (lambda (x) 0)) sexpr))
)


;; Using the instructions provided on LEARN, give a defintion for count
(define count
  (lambda (pred sexpr)
    ((fold-tree + 0 (lambda (x) (if (pred x) 1 0))) sexpr)))


;; ========== Flatten Tree ==========

;; Using the instructions provided on LEARN, give a defintion for flatten-tree
(define flatten-tree
  (lambda (ls)
    ((fold-tree append '() (lambda (x) (list x))) ls))
)


;; Using the instructions provided on LEARN, give a defintion for map-tree
(define map-tree
  (lambda (proc ls)
    ((fold-tree cons '() proc) ls))
)
