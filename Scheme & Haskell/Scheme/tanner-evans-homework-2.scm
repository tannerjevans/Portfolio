(define name '("Evans" "Tanner"))
(define netID "tannerjevans@unm.edu")


;; problem1-4.4    p129
;; Points: 1/15
;; (deepen-1 '(a b c)) ;; => '((a) (b) (c))
(define deepen-1
  (lambda (ls)
    (if (null? ls)
	ls
	(cons (cons (car ls)
		    '())
	      (deepen-1 (cdr ls)))))
)


;; problem1-4.6    p136
;; Points: 1/15
;; (insert-left-all 'z 'a '(a ( (b a) ((a (c)))))) ;; => '(z a ((b z a) ((z a (c)))))
(define insert-left-all
  (lambda (new old ls)
    (cond
     ((null? ls) ls)
     ((pair? (car ls))
      (cons (insert-left-all new old (car ls))
	    (insert-left-all new old (cdr ls))))
     ((equal? (car ls) old)
      (cons new (cons old (insert-left-all new old (cdr ls)))))
     (else (cons (car ls) (insert-left-all new old (cdr ls))))))
)


;; problem1-4.10    p143
;; Points: 1/15
;; (leftmost '((a b) (c (d e)))) ;; => 'a
(define leftmost
  (lambda (ls)
    (if (pair? (car ls))
	(leftmost (car ls))
	(car ls)))
)


;; problem1-4.11    p143
;; Points: 1/15
;; (rightmost '((a b) (d (c d (f (g h) i) m n) u) v)) ;; => 'v
(define rightmost
  (lambda (ls)
    (cond
     ((not (pair? ls)) ls)
     ((null? (cdr ls)) (rightmost (car ls)))
     (else (rightmost (cdr ls)))))
)


;; problem1-4.18    p156
;; Points: 1/15
;; (length-it '(1 2 3)) ;; => 3
(define lengthIt
  (lambda (ls nAcc)
    (if (null? ls)
	nAcc
	(lengthIt (cdr ls) (add1 nAcc)))))
(define length-it
  (lambda (ls)
    (lengthIt ls 0))
)


;; listify-it:
;; A helper procedure for problem1-4.19 to eliminate non-
;; iterative procedure calls in an iterative procedure,
;; and to enable constant-time construction of result and
;; linear-time conversion to correct result once at end.
(define listify-it
  (lambda (ls lsAcc)
    (if (null? ls)
	lsAcc
	(listify-it (car ls) (cons (cdr ls) lsAcc)))))

;; problem1-4.19    p156
;; Points: 1/15
;; (mk-asc-list-of-ints 10) ;; => '(1 2 3 4 5 6 7 8 9 10)
(define mk-asc-list-of-ints-it
  (lambda (n lsAcc len)
    (if (= n len)
        (listify-it (cons lsAcc n) '())
	(mk-asc-list-of-ints-it n (cons lsAcc len) (add1 len)))))
(define mk-asc-list-of-ints
  (lambda (n)
    (if (and (integer? n) (> n 0))
	(mk-asc-list-of-ints-it n '() 1)
	"Error: mk-asc-list-of-ints requires an integer argument of n > 0."))
)


;; Points: 1/15
;; (mk-desc-list-of-ints 10) ;; => '(10 9 8 7 6 5 4 3 2 1)
(define mk-desc-list-of-ints-it
  (lambda (n lsAcc)
    (if (= n 1)
	(listify-it (cons lsAcc n) '())
	(mk-desc-list-of-ints-it (sub1 n) (cons lsAcc n)))))
(define mk-desc-list-of-ints
  (lambda (n)
    (if (and (integer? n) (> n 0))
	(mk-desc-list-of-ints-it n '())
	"Error: mk-desc-list-of-ints requires an integer argument of n > 0."))
)


;; problem1-4.20    p156
;; Points: 1/15
;; (occurs 'a '(a b a c a d)) ;; => 3
(define occurs
  (lambda (item ls)
    (cond
     ((null? ls) 0)
     ((equal? item (car ls)) (+ 1 (occurs item (cdr ls))))
     (else (occurs item (cdr ls)))))
)


;; Points: 1/15
;; (occurs-it 'a '(a b a c a d)) ;; => 3
(define occursIt
  (lambda (item ls nAcc)
    (cond
     ((null? ls) nAcc)
     ((equal? item (car ls)) (occursIt item (cdr ls) (add1 nAcc)))
     (else (occursIt item (cdr ls) nAcc)))))
(define occurs-it
  (lambda (item ls)
    (occursIt item ls 0))
)


;; problem2-
;; Points: 1/15
;; (calculator '(1 + (2 * 8))) ;; => 17
(define calculator
  (lambda (ls)
    (cond
     ((not (pair? ls)) ls)
     ((eqv? (cadr ls) '+)
      (+ (calculator (car ls)) (calculator (caddr ls))))
     ((eqv? (cadr ls) '-)
      (- (calculator (car ls)) (calculator (caddr ls))))
     ((eqv? (cadr ls) '/)
      (/ (calculator (car ls)) (calculator (caddr ls))))
     (else
      (* (calculator (car ls)) (calculator (caddr ls))))))
)


;; problem3-
;; Points: 1/15
;; (infix->prefix '(1 + (2 * 8))) ;; => '(+ 1 (* 2 8))
(define infix->prefix
  (lambda (ls)
    (if (pair? ls)
	(list (cadr ls)
	      (infix->prefix (car ls))
	      (infix->prefix (caddr ls)))
	ls))
)


;; problem4-
    ;; NOTE: All helper functions should be tail-recursive and should be defined within the body of iota-iota using letrec.
;; Points: 1/15
;; (iota-iota 5) ;; => '((1 . 1) (1 . 2) (1 . 3) (1 . 4) (1 . 5) (2 . 1) (2 . 2) (2 . 3) (2 . 4) (2 . 5) (3 . 1) (3 . 2) (3 . 3) (3 . 4) (3 . 5) (4 . 1) (4 . 2) (4 . 3) (4 . 4) (4 . 5) (5 . 1) (5 . 2) (5 . 3) (5 . 4) (5 . 5))
(define iota-iota
  (lambda (n)
    (letrec ((listifyIt
	      (lambda (ls lsAcc)
		(if (null? ls)
		    lsAcc
		    (listifyIt (car ls)
			       (cons (cdr ls) lsAcc)))))
	     (build
	      (lambda (nLim lsAcc nCar nCdr)
		(cond
		 ((= nCar nCdr nLim)
		  (listifyIt (cons lsAcc (cons nCar nCdr))
			     '()))
		 ((= nCdr nLim)
		  (build nLim
			 (cons lsAcc (cons nCar nCdr))
			 (+ nCar 1)
			 1))
		 (else
		  (build nLim
			 (cons lsAcc (cons nCar nCdr))
			 nCar
			 (+ nCdr 1)))))))
	  (build n '() 1 1)))
)


;; problem5-
    ;; NOTE: Any helper functions you need should be defined within the body of digits->number using letrec.
;; Points: 1/15
;; (digits->number '(7 6 1 5)) ;; => 7615
(define digits->number
  (lambda (ls)
    (letrec ((digToNum
	      (lambda (ls nAcc)
		(if (null? ls)
		    nAcc
		    (digToNum (cdr ls) (+ (* nAcc 10) (car ls)))))))
      (digToNum ls 0)))
)



;; problem6-
;; Points: 1/15
;; (cond->if '(cond ((> x y) (- x y)) ((< x y) (- y x)) (else 0))) ;; => '(if (> x y) (- x y) (if (< x y) (- y x) 0))
(define cond->if
  (lambda (ls)
    (letrec ((condToIf
	      (lambda (ls)
		(if (equal? (caadr ls) 'else)
		    (cons 'if
			  (cons (caar ls)
				(cons (cadar ls)
				      (cons (cadadr ls)
					    '()))))
		    (cons 'if
			  (cons (caar ls)
				(cons (cadar ls)
				      (cons (condToIf (cdr ls))
					    '()))))))))
      (condToIf (cdr ls))))
)



;; problem7-
    ;; NOTE: Do not use or define fact or expt, any helper functions you need should be defined within the body of cos using letrec
;; Points: 1/15
;; (cos 3.14159) ;; => -0.9999999999964797
(define cos
  (lambda (x)
    (letrec ((tay
	      (lambda (sumAcc prevSign xSq xAcc factAcc iter)
		(let*
		    ((newIter (+ 2 iter))
		     (newFactAcc (* factAcc iter (sub1 iter)))
		     (newXAcc (* xAcc xSq))
		     (newSign (* -1 prevSign))
		     (newSumAcc (+ sumAcc (* newSign (/ newXAcc newFactAcc)))))
		  (if (>= iter 200)
		      sumAcc
		      (tay newSumAcc newSign xSq newXAcc newFactAcc newIter))))))
      (tay 1 1 (* x x 1.0) 1 1 2)))
)
