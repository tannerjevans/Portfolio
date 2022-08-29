(define name '("Evans" "Tanner"))
(define netID "tannerjevans@unm.edu")


;; problem 1.2, page 13
(define problem1.2
    '( ;; Tanner
        ('a "10500900")
        ('b "2.5e-007")
        ('c "'big-number")
        ('d "'cat")
        ('e "'cheshire")
        ('f "10500900")
        ('g "'big-number")
        ('h "'number1")
    )
)


;; problem 1.3, page 13
(define problem1.3
    '(
        ('a 4)
        ('b 2/5)
        ('c 2/3)
        ('d 0.6666666666666667)
    )
)


;; problem 1.4, page 13
(define problem1.4
    '( ;; Tanner 
        ('a (- (* 4 7) (+ 13 5)))
        ('b (* 3 (+ 4 (- -5 -3))))
        ('c (/ 2.5 (* 5 (/ 1 10))))
        ('d (* 5 (+ 255 (* 537 (+ 98.3 (- 375 (* 2.5 153)))))))
    )
)


;; problem 1.5, page 14
(define problem1.5
    '(
        ('a a + ((b + g) - a))
        ('b (a * b) + (g * b))
        ('c (a - b)/(a - g))
    )
)


;; problem 1.6, page 19
(define problem1.6
    '(
        ('a
         (cons 'one
	       (cons 'two
		     (cons 'three
			   (cons 'four '())))))
        ('b
	 (cons 'one
	       (cons (cons 'two
			   (cons 'three
				 (cons 'four '())))
		     '())))
        ('c
	 (cons 'one
	       (cons (cons 'two
			   (cons 'three '()))
		     (cons 'four '()))))
        ('d
	 (cons (cons 'one
		     (cons 'two '()))
	       (cons (cons 'three
			   (cons 'four '()))
		     '())))
        ('e
	 (cons (cons (cons 'one '())
		     '())
	       '()))
    )
)


;; problem 1.10, page 25
(define problem1.10
    '(
        ('a #f)
        ('b #t)
        ('c #f)
        ('d #t)
    )
)


;; problem 1.14, page 30
(define problem1.14
    '(
        ('a #t)
        ('b #f)
        ('c #f)
        ('d #t)
        ('e #f)
        ('f #t)
    )
)


;; problem 2.1, page 39
(define second
  (lambda (list)
    (cadr list))
)


;; problem 2.3, page 39
(define make-list-of-one
  (lambda (item)
    (cons item '()))
)
(define make-list-of-two
  (lambda (item1 item2)
    (cons item1 (make-list-of-one item2)))
)
(define firsts-of-both
  (lambda (list-1 list-2)
    (make-list-of-two (car list-1) (car list-2)))
)
(define problem2.3
    '(
        ('a '(1 2))
        ('b '((a b) (e f)))
    )
)


;; problem 2.4, page 39
(define juggle
  (lambda (list)
    (cons (caddr list)
	  (cons (car list)
		(cons (cadr list) '()))))
)


;; problem 2.6, page 45
(define a #t)
(define b #t)
(define c #t)
(define e #f)
(define f #f)
(define problem2.6
    '(
        ('a #t)
        ('b #t)
        ('c #t)
        ('d #f)
    )
)


;; problem 2.7, page 45
(define expr #f)
(define problem2.7
    '(
        ('a #t)
        ('b #f)
        ('c #t)
        ('d #f)
    )
)


;; problem 2.10, page 53
(define last-item
  (lambda (ls)
    (if (null? (cdr ls))
	(car ls)
	(last-item (cdr ls))))
)
(define member?
  (lambda (item ls)
    (if (null? ls)
	#f
	(or (equal? (car ls) item)
	    (member? item (cdr ls)))))
)
(define remove-1st
  (lambda (item ls)
    (if (null? ls)
	'()
	(if (equal? (car ls) item)
	    (cdr ls)
	    (cons (car ls)
		  (remove-1st item (cdr ls))))))
)


;; problem 2.12, page 54
(define mystery
    (lambda (ls)
        (if (null? (cddr ls))
            (cons (car ls) '())
            (cons (car ls)
		  (mystery (cdr ls)))
        )
    )
)
(define problem2.12
    '(
        ('a '(1 2 3 4))
        ('b "Removes the last item of the list.") ;; BEHAVIOR
        ('b "remove-last") ;; REASONABLE NAME
    )
)


;; problem 2.13, page 54
(define subst-1st
  (lambda (new old ls)
    (cond
     ((null? ls) ls)
     ((equal? (car ls) old)
      (cons new
	    (cdr ls)))
     (else (cons (car ls)
		 (subst-1st new old (cdr ls))))))
)
(define substq-1st
  (lambda (new old ls)
    (cond
     ((null? ls) ls)
     ((eq? (car ls) old)
      (cons new
	    (cdr ls)))
     (else (cons (car ls)
		 (subst-1st new old (cdr ls))))))
)
(define substv-1st
  (lambda (new old ls)
    (cond
     ((null? ls) ls)
     ((eqv? (car ls) old)
      (cons new
	    (cdr ls)))
     (else (cons (car ls)
		 (subst-1st new old (cdr ls))))))
)


;; problem 2.14, page 55
(define insert-left-1st
  (lambda (new old ls)
    (cond
     ((null? ls) ls)
     ((equal? (car ls) old)
      (cons new ls))
     (else (cons (car ls)
		 (insert-left-1st new old (cdr ls))))))
)


;; problem 2.15, page 55
(define list-of-first-items
  (lambda (ls)
    (if (null? ls)
	ls
	(cons (caar ls)
	      (list-of-first-items (cdr ls)))))
)


;; problem 2.16, page 56
(define replace
  (lambda (new-item ls)
    (if (null? ls)
	ls
	(cons new-item
	      (replace new-item (cdr ls)))))
)


;; problem 2.18, page 56
(define remove-last
  (lambda (item ls)
    (cond
     ((null? ls) ls)
     ((and (equal? (car ls) item)
	   (equal? (remove-last item (cdr ls)) (cdr ls)))
      (cdr ls))
     (else (cons (car ls)
		 (remove-last item (cdr ls))))))
)
