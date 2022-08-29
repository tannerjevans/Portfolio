
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


;; Using the instructions provided on LEARN, give a defintion for unbounce
(define unbounce
  (lambda (ls)
    (cond
     ((null? (cdr ls))
      ls)
     ((eq? (car ls) (cadr ls))
      (unbounce (cdr ls)))
     (else
      (cons (car ls) (unbounce (cdr ls))))))
)

;; Using the instructions provided on LEARN, give a defintion for deep-member?
(define deep-member?
  (lambda (item ls)
    (cond
     ((null? ls)
      #f)
     ((pair? ls)
      (or (deep-member? item (car ls))
	  (deep-member? item (cdr ls))))
     ((eq? item ls) #t)
     (else #f)))
)

;; Using the instructions provided on LEARN, give a defintion for vowels
(define vowels
  (lambda (ls)
    (letrec
	((vls '(a e i o u))
	 (vl?
	  (lambda (item ls1)
	    (cond
	     ((null? ls1)
	      0)
	     ((eq? item (car ls1))
	      1)
	     (else
	      (vl? item (cdr ls1))))))
	 (loop
	  (lambda (ls2 acc)
	    (if (null? ls2)
		acc
		(loop (cdr ls2) (+ acc (vl? (car ls2) vls)))))))
      (loop ls 0)))
)



;; Fill out the following fields, giving answers as expressions
;; Each defintion comes pre-filled as '(), delete the '() prior to
;;  filling out your response.
;; Assuming the answer was the value '(5 6), the following definitions are WRONG:
;;  (define value ''(5 6)) as the value is quoted
;;  (define value '('(5 6))) as the value is a quoted list
;;  (define value ('(5 6))) as the value is being evaluated
;;  (define value '((5 6))) as the value is incorrect
;; Assuming the answer was the value '(5 6), the following definition is CORRECT:
;; (define value '(5 6))
;; Assuming the answer was the value 3, the following definition is CORRECT:
;; (define value 3)

;; Using the instructions provided on LEARN, give the value of the expression
(define value-of-expression
  4
)

;; Using the instructions provided on LEARN, give a defintion for the expression using let instead of let*
(define expression-using-let
  (let
      ((x 2))
    (let
	((y x)
	 (x 1))
      (let
	  ((x y))
	(+ x x))))
)

;; Using the instructions provided on LEARN, give a defintion for the expression using lambda instead of let*
(define expression-using-lambda
  ((lambda (x)
     ((lambda (y x)
	((lambda (x)
	   (+ x x))
	 y))
      x 1))
   2)
)
