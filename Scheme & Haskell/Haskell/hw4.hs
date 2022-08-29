
studentName = ["Evans", "Tanner"]
netID = "tannerjevans@unm.edu"


-- Problem 1, stutter
stutter :: [a] -> [a]
stutter ls = concat [ [x,x] | x <- ls ]

p1tests = [(stutter "Hello World") == "HHeelllloo  WWoorrlldd", (stutter [1,2,3]) == [1,1,2,2,3,3]]


-- Problem 2, compress
compress :: Eq a => [a] -> [a]
compress (h:[]) = [h]
compress (h:t) = 
    if head t == h 
        then compress t 
        else h : compress t

p2tests = [compress "HHeelllloo WWoorrlldd" == "Helo World",  compress [1,2,2,3,3,3] == [1,2,3]]


-- Problem 3, findIndices
findIndices :: (a -> Bool) -> [a] -> [Int]
findIndices pred ls = [ x | (x, y) <- (zip [0..] ls), pred y ]

p3tests = [findIndices (< 'a') "AbCdef" == [0,2],findIndices (== 0) [1,2,0,3,0] == [2,4]]


-- Problem 3.5, intersect
intersect :: Eq a => [a] -> [a] -> [a]
intersect ls1 ls2 = [ x | x <- ls1, x `elem` ls2]

p35tests = [intersect "abc" "cat" == "ac", intersect [1,2,3] [8] == [], intersect [3,2,1] [1,2,3] == [3,2,1]]


-- Problem 4, isPrefixOf
isPrefixOf :: Eq a => [a] -> [a] -> Bool
isPrefixOf ls1 ls2 = ls1 == take (length ls1) ls2

p4tests = ["foo" `isPrefixOf` "foobar", not $ isPrefixOf [1,2,3] [4,5,6]]


-- Problem 5, isSuffixOf
isSuffixOf :: Eq a => [a] -> [a] -> Bool
isSuffixOf ls1 ls2 = ls1 == drop (length ls2 - length ls1) ls2

p5tests = ["bar" `isSuffixOf` "foobar", not $ isSuffixOf [1,2,3] [4,5,6]]


-- Problem 6, dot
dot :: [Int] -> [Int] -> Int
dot ls1 ls2 = sum [ x * y | (x, y) <- (zip ls1 ls2) ]

p6tests = [[0,0,1] `dot` [0,1,0] == 0]


-- Problem 7, increasing
increasing :: (Ord a) => [a] -> Bool
increasing ls = and [ x < y | (x, y) <- (zip ls (tail ls)) ]

p7tests = [increasing "ABCD", not $ increasing [100,99..1]]


-- Problem 8, decimate
decimate :: [a] -> [a]
decimate ls = [ x | (x, y) <- (zip ls (cycle [1..10])), y /= 10 ]

p8tests = [decimate [1..21] == [1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,21]]


-- Problem 9, encipher
encipher :: Eq a => [a] -> [b] -> [a] -> [b]
encipher ls1 ls2 ls3 = [ getVal x | x <- ls3 ]
    where cipher = (zip ls1 ls2)
          getVal key = head [ y | (x, y) <- cipher, x == key ]

p9tests = [encipher ['A'..'Z'] ['a'..'z'] "THIS" == "this"]


-- Problem 10, prefixSum
prefixSum :: (Num a) => [a] -> [a]
prefixSum ls = 
    let h = head ls 
    in h : recur h (tail ls)
    where recur _ [] = []
          recur prev curr = 
              let sum = (head curr + prev) 
              in sum : recur sum (tail curr)

p10tests = [prefixSum [1..10] == [1,3,6,10,15,21,28,36,45,55], prefixSum [2, 5] == [2, 7]]


-- Problem 11, select
select :: (t -> Bool) -> [t] -> [a] -> [a]
select pred ls1 ls2 = [ y | (x, y) <- (zip ls1 ls2), pred x ]

p11tests = [select even [1..26] "abcdefghijklmnopqrstuvwxyz" == "bdfhjlnprtvxz", select (<= 'g') "abcdefghijklmnopqrstuvwxyz" [1..26] == [1,2,3,4,5,6,7]]


-- Problem 12, numbers
numbers :: [Int] -> Int
numbers ls = recur 1 0 (reverse ls)
    where recur _ acc [] = acc
          recur power acc nums = 
              let nextPow = 10 * power;
                  nextAcc = acc + power * head nums;
                  nextNums = tail nums
              in recur nextPow nextAcc nextNums

p12tests = [ numbers [1..4] == 1234 ]


tests = [p1tests,p2tests,p3tests,p35tests,p4tests,p5tests,p6tests,p7tests,p8tests,p9tests,p10tests,p11tests,p12tests]
likelyCorrect = and $ map and tests