(define compose
  (lambda (f g)
    (lambda (x)
      (f (g x)))))

(define iterate
  (lambda (f n)
    (if (= n 0)
	(lambda (x) x)
	(compose f (iterate f (- n 1))))))

(define iterate-it
  (lambda (f n acc)
    (if (= n 0)
	acc
	(iterat-it f (- n 1) (compose f acc)))))

;; tail recursion internal call - USE THIS
(define iterate
  (lambda (f n)
    (letrec ((loop
	      (lambda (n acc)
		(if (= n 0)
		    acc
		    (loop (- n 1) (compose f acc))))))
      (loop n (lambda (x) x)))))

;;return nth item in list
(define list-ref
  (lambda (ls n)
    (if (= n 0)
	(car ls)
	(list-ref (cdr ls) (- n 1)))))
;;using iterate
(define list-ref
  (lambda (ls n)
    (car ((iterate cdr n) ls))))

;; get rth root of n
;; (rt 81 2) -> 9
;; (rt 8 3) -> 2
(define rt
  (lambda (n r)
    (let*
	((nWgt (/ 1 r))
	 (rWgt (* nWgt (- r 1)))
	 (lft 1)
	 (rgt 1))
      (letrec
	  ((findRange))))))

(define val '(((t)) (((e)) ((s)) t)))
(define answer
  (cons (cons (cons 't '()) '()) (cons (cons (cons (cons 'e '()) '()) (cons (cons (cons 's '()) '()) (cons 't '()))) '())))

(define participation
  (equal? val answer)
  )
participation


(define fibAnn
  (lambda (n)
    (let* ((rad (sqrt 5))
	   (invRad (/ 1 rad)))
      (round (- (* invRad (expt (/ (+ 1 rad) 2) n)) (* invRad (expt (/ (- 1 rad) 2) n)))))))

(define fibU
  (lambda (n)
    (let* ((rad (sqrt 5))
	   (invRad (/ 1 rad)))
      (* invRad (expt (/ (+ 1 rad) 2) n)))))

(define fibD
  (lambda (n)
    (let* ((rad (sqrt 5))
	   (invRad (/ 1 rad)))
      (* invRad (expt (/ (- 1 rad) 2) n)))))

(define fibListAnn
  (lambda (n)
    (list (fibU n) (fibD n) (fib n))))

(define fibRec
  (lambda (n)
    (letrec ((tailRec
	      (lambda (acc-1 acc-2 iter)
		(if (= iter n)
		    acc-2
		    (tailRec (acc-2 (+ acc-1 acc-2)))))))
      (tailRec 0 1 1))))

;; x + 0 -> x
;; x + y -> (x - 1) + (y + 1)
(define plus
  (lambda (x y)
    (if (= x 0)
	y
	(plus (sub1 x) (add1 y)))))

(define plus
  (lambda (x y)
    ((iterate add1 x) y)))

(define round-n-places
  (lambda (x n)
    (let ((pot (expt 10.0 n)))
      (/ (floor (+ (* x pot) 0.5)) pot))))

(define round-n-places-c
  (lambda (n)
    (let ((pot (expt 10.0 n)))
      (lambda (x)
	(/ (floor (+ (* x pot) 0.5)) pot)))))

(define round-2-places (round-n-places-c 2))
;; makes round-2-places the inner lambda with n filled in.
;; let to lambda 

(define round-n-places-c
  (lambda (n)
    ((lambda (pot)
      (lambda (x)
	(/ (floor (+ (* x pot) 0.5)) pot)))
     (expt 10.0 n))))
;;creates the anon fun that immediately takes a value

(define if-else
  (lambda (w x y z)
    (if (= w x)
	y
	z)))

(define if-else-c
  (lambda (w)
    (lambda (x)
      (lambda (y)
	(lambda (z)
	  (if (= w x)
	      y
	      z))))))

;; x = (x0, x1) and y = (y0, y1) then x.y = x0*y0 + x1*y1
(define dot-product
  (lambda (a b c d w x y z)
    (apply + (map * (list a b c d) (list w x y z)))))


(define dot-product-c
  (lambda (a b c d)
    (lambda (w x y z)
      (apply + (map * (list a b c d) (list w x y z))))))

(define dot-product-c
  (lambda v0
    (lambda v1
      (apply + (map * v0 v1)))))

(define delete
  (lambda (item ls)
    (cond
     ((null? ls)
      ls)
     ((equal? item (car ls))
      (cdr ls))
     (else
      (cons (car ls) (delete item (cdr ls)))))))

(define delete-duplicates
  (lambda (ls)
    (if (null? ls)
	ls
	(cons (car ls)
	      (delete (car ls) (delete-duplicates (cdr ls)))))))

(define prefix?
  (lambda (ls0 ls1)
    (or (null? ls0)
	(and (eq? (car ls0) (car ls1))
	     (prefix? (cdr ls0) (cdr ls1))))))
    
(define sublist?
  (lambda (ls0 ls1)
    (and (not (null? ls1))
	 (or (prefix? ls0 ls1)
	     (sublist? ls0 (cdr ls1))))))

(define test
  (lambda (ls)
    (let
	(
	 (map-add (lambda (ls) (map add1 ls)))
	 (remove-first cdr)
	 )
      (cons (map-add ls) (remove-first ls))
      )
    )
  )

(define test2
  (lambda (ls)
    ((lambda (map-add remove-first)
       (cons (map-add ls) (remove-first ls)))
     (lambda (ls) (map add1 ls)) cdr)))

(define test
  (lambda (ls)
    (let* ((map-add (lambda (i ls) (map (curry + i) ls)))
	   (remove-first cdr)
	   (add-five (lambda (ls) (map-add 5 ls))))
      (cons (add-five ls) (remove-first ls)))))

(define test2
  (lambda (ls)
    ((lambda (map-add)
       ((lambda (remove-first)
	  ((lambda (add-five)
	     (cons (add-five ls) (remove-first ls)))
	   (lambda (ls) (map-add 5 ls))))
	cdr))
     (lambda (i ls) (map (curry + i) ls)))))

(define swapper
  (lambda (x y ls)
    (cond
     ((null? ls)
      ls)
     ((equal? x (car ls))
      (cons y (swapper x y (cdr ls))))
     ((equal? y (car ls))
      (cons x (swapper x y (cdr ls))))
     (else
      (cons (car ls) (swapper x y (cdr ls)))))))

(define swapper
  (lambda (x y ls)
    (letrec
	((loop
	  (lambda (ls2)
	    (cond
	      ((null? ls2)
	       ls2)
	      ((equal? x (car ls2))
	       (cons y (loop (cdr ls2))))
	      ((equal? y (car ls2))
	       (cons x (loop (cdr ls2))))
	      (else
	       (cons (car ls2) (loop (cdr ls2))))))))
      (loop ls))))

(define test
  (lambda ()
    (let ((a 5))
      (let ((fun (lambda (x) (max x a))))
	(let ((a 10)
	      (x 20))
	  (fun 1))))))

(define test
  (lambda ()
    (letrec
	((loop
	  (lambda (n k)
	    (cond
	     ((zero? k)
	      n)
	     ((< n k)
	      (loop k n))
	     (else
	      (loop k (remainder n k)))))))
      (loop 9 12))))

(define fib-it
  (lambda (n)
    (if (< n 2)
	n
	(letrec
	    ((fib-up
	      (lambda (num acc1 acc2)
		(if (= num n)
		    acc2
		    (fib-up (add1 num) acc2 (+ acc1 acc2))))))
	  (fib-up 1 0 1)))))

(define fib
  (lambda (n)
    (if (< n 2)
	n
	(+ (fib (- n 1)) (fib (- n 2))))))



(define fib-2
  (lambda (n)
    (cond
     ((< n 2)
      n)
     ((= n 2)
      1)
     ((even? n)
      (let* ((half (/ n 2))
	     (halfn (fib-2 half)))
	(+ (sqr halfn) (* 2 halfn (fib-2 (sub1 half))))))
     (else
      (let* ((half (floor (/ n 2)))
	     (half+ (fib-2 (add1 half)))
	     (halfn (fib-2 half)))
	(+ (* half+ half+) (* halfn halfn)))))))


(define fib-mem
  (lambda (n)
    (letrec
	((get
	  (lambda (key ls)
	    (cond
	     ((null? ls)
	      ls)
	     ((= key (caar ls))
	      (cadar ls))
	     (else
	      (get key (cdr ls))))))
	 (put
	  (lambda (key val ls)
	    (cons (list key val) ls)))
	 (loop
	  (lambda (num fibs)
	    (let ((res (get num fibs)))
	      (if (null? res)
		  (cond
		   ((< num 2)
		    (put num num fibs))
		   ((= num 2)
		    (put num 1 fibs))
		   ((even? num)
		    (let* ((half (/ num 2))
			   (fibs1 (loop half fibs))
			   (halfn (get half fibs1))
			   (fibs2 (loop (sub1 half) fibs1))
			   (half- (get (sub1 half) fibs2)))
		      (put num (* halfn (+ halfn (* 2 half-))) fibs2)))
		   (else
		    (let* ((half (floor (/ num 2)))
			   (fibs1 (loop (add1 half) fibs))
			   (half+ (get (add1 half) fibs1))
			   (fibs2 (loop half fibs1))
			   (halfn (get half fibs2)))
		      (put num (+ (sqr half+) (sqr halfn)) fibs2))))
		  fibs)))))
      (get n (loop n '())))))

(define fib-num 100000000)
(define start-fib-2 (current-milliseconds))
(define fib-2-ans (fib-2 fib-num))
(define start-fib-mem (current-milliseconds))
(define fib-mem-ans (fib-mem fib-num))
(define end (current-milliseconds))
(begin
  (newline)
  (display "(fib-2 ")
  (display fib-num)
  (display ")   took ")
  (display (- start-fib-mem start-fib-2))
  (display " ms")
  (newline)
  (display "(fib-mem ")
  (display fib-num)
  (display ") took ")
  (display (- end start-fib-mem))
  (display " ms")
  (newline))
(string-length (number->string fib-mem-ans))



(define count-digs
  (lambda (x)
    (letrec
	((loop
	  (lambda (x1 count)
	    (if (< x1 10)
		count
		(loop (/ x1 10) (add1 count))))))
      (loop x 1))))


((lambda (x y)
  (writeln x)
  (writeln y)
  (+ x y))
 1 2)

(define 6.5
  (lambda ()
    (begin
      (writeln "Enter a number:")
      (let ((in (read)))
	(if (eq? in 'stop)
	    (writeln "Stopped.")
	    (begin
	      (writeln "The square of " in " is " (sqr in) ".")
	      (writeln "The square root of " in " is " (sqrt in) ".")
	      (newline)
	      (6.5)))))))

(define interactive-square-root
  (lambda ()
    (writeln "Enter the number whose square root you want , or enter done to quit: ")
    (let ((n (read)))
      (if (eq? n 'done)
	  (writeln "That's all, folks.")
	  (begin
	    (write "The square root of " n " is " (sqrt n))
	    (newline)
	    (newline)
	    (interactive-square-root))))))

(define parity
  (lambda (n)
    (letrec
	((even?
	  (lambda (x)
	    (if (zero? x)
		'even
		(odd? (sub1 x)))))
	 (odd?
	  (lambda (y)
	    (if (zero? y)
		'odd
		(even? (sub1 y))))))
      (even? n))))

(define parity-lambda
  (lambda (n)
    ((lambda (even?)
       ((lambda (odd?)
	  ))
       
	
	
	((even?
	  (lambda (x)
	    (if (zero? x)
		'even
		(odd? (sub1 x)))))
	 (odd?
	  (lambda (y)
	    (if (zero? y)
		'odd
		(even? (sub1 y))))))
	(even? n)))))


(define add1-all
  (lambda (ls)
    (if (null? ls)
	ls
	(cons (add1 (car ls))
	      (add1-all (cdr ls))))))

(define wrap
  (lambda (sexpr)
    (if (pair? sexpr)
	sexpr
	(list sexpr))))

(define wrap-all
  (lambda (ls)
    (if (null? ls)
	ls
	(cons (wrap (car ls))
	      (wrap-all (cdr ls))))))

(define evens?
  (lambda (ls)
    (if (null? ls)
	ls
	(cons (even? (car ls))
	      (evens? (cdr ls))))))

(define map
  (lambda (proc ls)
    (if (null? ls)
	ls
	(cons (proc (car ls))
	      (map proc (cdr ls))))))




(define map-c
  (lambda (proc)
    (lambda (ls)
      (if (null? ls)
	  '()
	  (cons (proc (car ls))
		((map-c proc) (cdr ls)))))))

(define delete-c
  (lambda (item)
    (lambda (ls)
      (if (null? ls)
	  ls
	  (if (eq? item (car ls))
	      ((delete-c item) (cdr ls))
	      (cons (car ls)
		    ((delete-c item) (cdr ls))))))))

(define append-c
  (lambda (ls1)
    (lambda (ls0)
      (if (null? ls0)
	  ls1
	  (cons (car ls0)
		((append-c ls1)
		 (cdr ls0)))))))

(define length
  (lambda (ls)
    (if (null? ls)
	0
	(+ 1 (length (cdr ls))))))



;; right-fold
(define fold
  (lambda (seed proc)
    (lambda (ls)
      (if (null? ls)
	  seed
	  (proc (car ls)
		((fold seed proc) (cdr ls)))))))

;; left-fold
(define l-fold
  (lambda (seed proc)
    (lambda (ls)
      (if (null? ls)
	  seed
	  (proc ((l-fold seed proc) (cdr ls))
		(car ls))))))


;;shallow
(define reverse
  (lambda (ls)
    (if (null? ls)
	ls
	(append (reverseall (cr ls))
		(list (car ls))))))



(define reverse-all
  (lambda (ls)
    (cond
     ((null? ls)
      ls)
     ((pair? (car ls))
      (append (reverse-all (cdr ls))
	      (list (reverse-all (car ls)))))
     (else
      (append (reverse-all (cdr ls))
	      (list (car ls)))))))

(define wrap
  (lambda (sexpr)
    (if (pair? sexpr)
	sexpr
	(list sexpr))))

(define flatten
  (lambda (ls)
    (if (null? ls)
	'()
	(append (wrap (car ls))
		(flatten (cdr ls))))))

(define reverse-all
  (lambda (ls)
    (cond
     ((null? ls)
      ls)
     ((pair? (car ls))
      (append (reverse-all (cdr ls))
	      (list (reverse-all (car ls)))))
     (else
      (append (reverse-all (cdr ls))
	      (list (car ls)))))))

(define map
  (lambda (proc ls)
    (if (null? ls)
	ls
	(cons (proc (car ls))
	      (map proc (cdr ls))))))

;;rewrite
(define reverse-all
  (lambda (ls)
    (if (pair? ls)
	(reverse (map reverse-all ls))
	ls)))

(define flatten-all
  (lambda (ls)
    (if (pair? ls)
	(flatten (map flatten-all ls))
	ls)))

(define flatten-all
  (lambda (ls)
    (cond
     ((null? ls)
      '())
     ((pair? (car ls))
      (append (flatten-all (car ls))
	      (flatten-all (cdr ls))))
     (else
      (cons (car ls)
	    (flatten-all (cdr ls)))))))


(define make-deep
  (lambda (proc)
    (lambda (ls)
      (if (pair? ls)
	  (proc (map (make-deep proc) ls))
	  ls))))

;; verbessertes Verfahren:

(define make-deep
  (lambda (proc)
    (letrec
	((pattern
	  (lambda (ls)
	    (if (pair? ls)
		(proc (map pattern ls))
		ls))))
      pattern)))



(define reverse-all (make-deep reverse))
(define flatten-all (make-deep flatten))

(define foo
  (lambda (ls)
    (let ((op (car ls)))
      (apply
       (cond ((eq? op '+) +)
	     ((eq? op '*) *)
	     ((eq? op '-) -)
	     (else /))
       (cdr ls)))))

(define calculator
  (lambda (ls)
    (if (not (pair? ls))
	ls
	((
       
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

(define evaluate (make-deep foo))




(define replace-f-c
  (lambda (old new)
    (fold '()
	  (lambda (x y)
	    (cons (if (eq? x old)
		      new
		      x)
		  y)))))

(define replace-f
  (lambda (old new ls)
    ((fold '()
	  (lambda (x y)
	    (cons (if (eq? x old)
		      new
		      x)
		  y)))
    ls)))

(define append2
  (lambda (ls0 ls1)
    ((fold ls1 cons) ls0)))

(define append
  (lambda args
    ((fold '() append2) args)))

;;using fold for stuff

(define map-c
  (lambda (proc)
    (fold '() (lambda (x y) (cons (proc x) y)))))

(define delete-c
  (lambda (item)
    (fold '() (lambda (x y) (if (eq? x item) y (cons x y))))))

(define length
  (fold 0 (lambda (x y) (+ y 1))))

(define append-c
  (lambda (ls1)
    (fold ls1 cons)))
;;sammeeeee
(define append-c
  (lambda (ls1)
    (fold ls1 (lambda (x y) (cons x y)))))

(define append2
  (lambda (ls0 ls1)
    ((fold ls1 cons) ls0)))



;;new things with fold

(define snoc
  (lambda (x ls)
    (append ls
	    (list x))))

(define reverse
  (fold '() snoc))

(define max2
  (lambda (x y)
    (if (> x y)
	x
	y)))

(define max
  (lambda (x . args)
    ((fold x max2) args)))

(define append
  (lambda args
    ((fold '() append2) args)))

(define append (fold '() append2))

(define compose2
  (lambda (f g)
    (lambda (x) (f (g x)))))

(define compose
  (lambda args
    ((fold (lambda (x) x) compose2) args)))

;;fold practice

(define number-of-f
  (lambda (item ls)
    ((fold 0
	   (lambda (x y)
	     (+ (if (eq? item x)
		    1
		    0)
		y)))
     ls)))
		




;;variadic abstract
(define make-variadic
  (lambda (seed proc2)
    (lambda args ((fold seed proc2) args))))

;;> > (((make-variadic (lambda (x) x) compose2) add1 add1 add1) 0)
;;3


(define wrap-all
  (lambda (ls)
    (map wrap ls)))

(define delete
  (lambda (item ls)
    (cond
     ((null? ls)
      ls)
     ((eq? item (car ls))
      (delete item (cdr ls)))
     (else
      (cons (car ls)
	    (delete item (cdr ls)))))))

(define length
  (lambda (ls)
    (if (null? ls)
	0
	(+ 1 (length (cdr ls))))))

(define append
  (lambda (ls0 ls1)
    (if (null? ls0)
	ls1
	(cons (car ls0)
	      (append (cdr ls0)
		      ls1)))))

(map (lambda (ls) (delete 1 ls)) '((1 2 3) (4 5 1) (a 1 3)))


(let* ((x 2) (y x) (x 1) (x y)) (+ x x))

(apply + '(1 2 3 4))



(define fold
  (lambda (seed proc)
    (lambda (ls)
      (if (null? ls)
	  seed
	  (proc (car ls)
		((fold seed proc) (cdr ls)))))))

;; Define the following functions
;;   each using a single line consisting
;;   of a single call to reduce (foldr)
;;      for an internal lambda,
;;      use (x ls) as the parameter set


(define test '(1 2 3 4 5))
(define list-reduce
    (lambda args
        (foldr cons '() args)
    )
)
(equal? (apply list-reduce test) test)


(define +-reduce
    (lambda args
        (foldr + 0 args)
    )
)
(equal? (apply +-reduce test) (apply + test))


(define filter-reduce
    (lambda (pred? . args)
        (foldr (lambda (x ls) (if (pred? x) (cons x ls) ls))  '() args)
    )
)
(equal? (apply filter-reduce (cons even? test)) (filter even? test))


(define map-reduce
    (lambda (f ls)
        (foldr (lambda (x ls) (cons (f x) ls)) '() ls)
    )
)
(equal? (map-reduce odd? test) (map odd? test))


(define map
  (lambda (proc ls)
    (if (null? ls)
	'()
	(cons (proc (car ls))
	      (map proc (cdr ls))))))

(define curry
  (lambda (proc . args)))

(define iota
  (lambda (n)
    (letrec
	((loop
	  (lambda ( n aa)



	    ))))))






(define box-fit
  (lambda (x1 x2 x3)
    (lambda (n)
      (letrec
	  ((dyn
	    (lambda ())))))))

(define longer
  (lambda (ls1 ls2)
    (if (> (length ls1) (length ls2))
	ls1
	ls2)))

(define longest
  (lambda args
    (cond ((null? args) '())
	  ((null? (cdr args)) (car args))
	  (else
	   (longer (car args)
		   (apply longest (cdr args)))))))

(define either?
  (lambda (x y)
    (or x y)))
;; to map or

(define any?
  (lambda args
    (cond ((null? args) #f)
	  ((null? (cdr args)) (car args))
	  (else
	   (either? (car args)
		    (apply any? (cdr args)))))))

(define compose
  (lambda (f g)
    (lambda (x) (f (g x)))))

(define compose-many
  (lambda args
    (cond ((null? args) (lambda (x) x))
	  ((null? (cdr args)) (car args))
	  (else
	   (compose (car args)
		    (apply compose-many (cdr args)))))))

(define generalize
  (lambda (proc seed)
    (lambda args
      (cond ((null? args) seed)
	    ((null? (cdr args)) (car args))
	    (else
	     (proc (car args)
		   (apply (generalize proc seed) (cdr args))))))))

(define generalize
  (lambda (proc seed)
    (letrec
	((pattern
	  (lambda args
	    (cond ((null? args) seed)
		  ((null? (cdr args)) (car args))
		  (else
		   (proc (car args)
			 (apply pattern (cdr args))))))))
      pattern)))

(define longest
  (generalize longer '()))

(define any? (generalize either? #f))

(define compose-many (generalize compose (lambda (x) x)))

(define flip
  (lambda (f)
    (lambda (x y) (f y x))))

(define fold
  (lambda (proc seed)
    (lambda (ls)
      (if (null? ls)
	  seed
	  (proc (car ls)
		((fold proc seed) (cdr ls)))))))
