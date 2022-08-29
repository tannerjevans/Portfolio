
studentName = ["Evans", "Tanner"]
netID = "tannerjevans@unm.edu"

-- Note: you cannot put a = b, where b is some built in function. I.e. no myTakeWhile = takeWhile
-- Problem 1, myTakeWhile
myTakeWhile :: (a-> Bool ) -> [a] -> [a]
myTakeWhile _ [] = []
myTakeWhile f (x:xs) =
    if f x then x : myTakeWhile f xs
    else []

p1tests = [myTakeWhile (/= ' ') "This is practice." == "This"]


-- Problem 2, mySpan
mySpan :: (a->Bool) -> [a] -> ([a],[a])
mySpan _ [] = ([], [])
mySpan f ls = 
    let left  = myTakeWhile f ls;
        right = drop (length left) ls
    in (left, right)

p2tests = [mySpan (/= ' ') "This is practice." == ("This"," is practice.")]


-- Problem 3, combinations3
combinations3 :: Ord a => [a] -> [[a]]
combinations3 ls = 
    let ls1 = zip [1..] ls
    in  [ [p,q,r] | (x,p) <- ls1, (y,q) <- ls1, (z,r) <- ls1, x < y, y < z ]

p3tests = [combinations3 "ABCDE" == ["ABC","ABD","ABE","ACD","ACE","ADE","BCD","BCE","BDE","CDE"]]


-- Problem 4, runLengthEncode
runLengthEncode :: Eq a => [a] -> [(a, Int)]
runLengthEncode [] = []
runLengthEncode ls = 
    let (x:xs) = ls;
        run [] = 0
        run ls1 = if head ls1 == x  then 1 + run (tail ls1) else 0;
        len = run ls
    in (x, len) : (runLengthEncode $ drop len ls)

p4tests = [runLengthEncode [4,2,2,1,1,1,1,4,4,4,4] == [(4,1),(2,2),(1,4),(4,4)], runLengthEncode "foo" == [('f',1),('o',2)]]


-- Problem 5, runLengthDecode
runLengthDecode :: [(a, Int)] -> [a]
runLengthDecode ls = concat [ replicate n a | (a, n) <- ls ]

p5tests = [runLengthDecode [(4,1),(2,2),(1,4),(4,4)] == [4,2,2,1,1,1,1,4,4,4,4], (runLengthDecode $ runLengthEncode "foobar") == "foobar"]


-- Problem 6, splitText
splitText :: Ord a => (a -> Bool) -> [a] -> [[a]]
splitText _ [] = []
splitText f ls = 
    let left  = myTakeWhile f ls;
        right = drop (succ . length $ left) ls
    in left : splitText f right

p6tests = [splitText (/= ' ') "This is practice." == ["This","is","practice."]]


-- Problem 7, encipher
encipher :: Eq a => [a] -> [b] -> [a] -> [b]
encipher ls1 ls2 ls3 = [ getVal x | x <- ls3 ]
    where cipher = (zip ls1 ls2)
          getVal key = head [ y | (x, y) <- cipher, x == key ]

p7tests = [encipher ['A'..'Z'] ['a'..'z'] "THIS" == "this",encipher [1..10] (map (\x -> x*x) [1..10]) [10,9..1] == [100,81,64,49,36,25,16,9,4,1],encipher [10,9..0] [10,9..0] [0..10] == [0,1,2,3,4,5,6,7,8,9,10],encipher (['A','C'..'Z'] ++ ['B','D'..'Z']) [1..26] ['A'..'Z'] == [1,14,2,15,3,16,4,17,5,18,6,19,7,20,8,21,9,22,10,23,11,24,12,25,13,26]]


-- Problem 8, goldbach
goldbach :: Int -> [(Int, Int)]
goldbach n =
    let prime q = [1,q] == [ x | x <- [1..q], q `mod` x == 0]
        primes = [ x | x <- [2..n], prime x]
    in [ (a,b) | a <- primes, b <- primes, (a + b) == n, a <= b]

p8tests = [goldbach 6 == [(3,3)], goldbach 14 == [(3,11),(7,7)]]


-- Problem 9, increasing
increasing :: Ord a => [a] -> Bool
increasing ls = and [ x <= y | (x, y) <- zip ls (tail ls) ]

p9tests = [increasing "ABBD", not $ increasing [100,99..1]]


-- Problem 10, select
select :: (t -> Bool) -> [t] -> [a] -> [a]
select pred ls1 ls2 = [ y | (x, y) <- (zip ls1 ls2), pred x ]

p10tests = [select even [1..26] "abcdefghijklmnopqrstuvwxyz" == "bdfhjlnprtvxz", select (<= 'g') "abcdefghijklmnopqrstuvwxyz" [1..26] == [1,2,3,4,5,6,7]]


-- Problem 11, combinations
combinations :: Ord a => Int -> [a] -> [[a]]
combinations _ [] = []
combinations 1 (x:xs) = [x] : combinations 1 xs
combinations n (x:xs) = map (x :) (combinations (pred n) xs) ++ combinations n xs

p11tests = [combinations 3 "ABCDE" == ["ABC","ABD","ABE","ACD","ACE","ADE","BCD","BCE","BDE","CDE"]]


-- Note: Uncomment the pNtests and in tests below and in tests once you have given a definiton for problem 12

-- Problem 12, ComplexInteger, real, imaginary
data ComplexInteger a = ComplexInteger { real :: a, imaginary :: a }

p12tests = [real (ComplexInteger 1 2) == 1, imaginary (ComplexInteger 2 3) == 3]


-- Problem 13, Eq
instance (Eq a) => Eq (ComplexInteger a) where
    (ComplexInteger x y) == (ComplexInteger i j) = (x == i) && (y == j)

p13tests = [(ComplexInteger 1 2) /= (ComplexInteger 3 4)]


-- Problem 14, Show
instance (Show a, Ord a, Num a) => Show (ComplexInteger a) where
    show (ComplexInteger 0 y) = show y ++ "i"
    show (ComplexInteger x 0) = show x
    show (ComplexInteger x y) = show x ++ (if y > 0 then "+" else "") ++ show y ++ "i"

p14tests = [(show $ ComplexInteger 1 2) == "1+2i", (show $ ComplexInteger 1 0) == "1", (show $ ComplexInteger 0 1) == "1i"]


-- Problem 15, Num
instance (Num a) => Num (ComplexInteger a) where
    (ComplexInteger x y) + (ComplexInteger i j) = (ComplexInteger (x + i) (y + j))
    (ComplexInteger x y) * (ComplexInteger i j) = (ComplexInteger (x*i - y*j) (x*j + y*i))

p15tests = [(ComplexInteger 1 2) * (ComplexInteger 3 4) == (ComplexInteger (-5) 10)]


tests = [p1tests,p2tests,p3tests,p4tests,p5tests,p6tests,p7tests,p8tests,p9tests,p10tests,p11tests] ++[p12tests,p13tests,p14tests,p15tests]
likelyCorrect = (and [and t | t <- tests], if length tests < 15 then "lacking ComplexInteger tests?" else "")