-- Problem 1, stutter
stutter :: [a] -> [a]
stutter ls = concat [ [x,x] | x <- ls ]
p1tests = [(stutter "Hello World") == "HHeelllloo  WWoorrlldd", (stutter [1,2,3]) == [1,1,2,2,3,3]]

-- Problem 2, compress
compress :: Eq a => [a] -> [a]
compress [h] = [h]
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

interleave :: [a] -> [a] -> [a]
interleave ls1 ls2 = concat [ [x,y] | (x,y) <- (zip ls1 ls2) ]

makeMap :: [a] -> [b] -> [(a,b)]
makeMap a b = zip a b

getVal :: Eq a1 => a1 -> [(a1,a2)] -> a2
getVal key cipher = head [ y | (x, y) <- cipher, x == key ]

getKey :: Eq a1 => a1 -> [(a2,a1)] -> a2
getKey val cipher = head [ x | (x, y) <- cipher, y == val ]

encipher' :: Eq a1 => [a1] -> [(a1, a)] -> [a]
encipher' src cipher = [ getVal x cipher | x <- src ]

decipher :: Eq a1 => [a1] -> [(a, a1)] -> [a]
decipher dest cipher = [ getKey x cipher | x <- dest ]

--describeList :: [a] -> String
describeList ls = "The list is " ++ case ls of 
    [] -> "empty."
    [x] -> "a singleton list."
    xs -> "a longer list."

replicate' n x 
    | n <= 0    = []
    | otherwise = x : replicate' (n-1) x

take' n _
    | n <= 0   = []
take' _ []     = []
take' n (h:t) = h : take' (n-1) t

quicksort :: Ord a => [a] -> [a]
quicksort [] = []
quicksort (h:t) = 
    let left  = [ x | x <- t, x <= h ];
        right = [ x | x <- t, x > h ]
    in quicksort left ++ [h] ++ quicksort right

mergesort :: Ord a => [a] -> [a]
mergesort [] = []
mergesort [x] = [x]
mergesort ls = 
    let split = floor $ fromIntegral $ (length ls) `div` 2;
        merge ls1 []        = ls1
        merge []  ls2       = ls2
        merge (x:xs) (y:ys) =
            if x < y then x : merge xs (y:ys)
            else          y : merge (x:xs) ys
    in merge (mergesort $ take split ls) (mergesort $ drop split ls)

--foldl uses tail recursion, accumulating answer
--foldl' :: (t1 -> t2 -> t1) -> t1 -> [t2] -> t1
foldl' _ acc []     = acc
foldl' f acc (x:xs) = foldl' f (f acc x) xs

--foldr recurses down to seed, calculates back up
foldr' :: (t1 -> t2 -> t2) -> t2 -> [t1] -> t2
foldr' _ seed []     = seed
foldr' f seed (x:xs) = f x (foldr' f seed xs)

reverse' :: [a] -> [a]
reverse' = foldl (\acc x -> x : acc) []

product' :: Num a => [a] -> a
product' = foldl (*) 1

filter' :: (a -> Bool) -> [a] -> [a]
filter' pred = foldr (\x acc -> if pred x then x : acc else acc) []

scanl' :: (b -> a -> b) -> b -> [a] -> [b]
scanl' f seed = foldl (\acc x -> acc ++ [f (last acc) x]) [seed]


fn = ceiling . negate . tan . cos . max 50

--review

compose :: (b -> c) -> (a -> b) -> (a -> c)
compose f g = \x -> f (g x)
-- without composition function

--4 : [5]
--(:) 4 [5]
--(flip (:)) [5] 4
-- can be useful for mapping

flip' :: (a -> b -> c) -> b -> a -> c
flip' f = \y x -> f x y

--iterate, basically
church :: (a -> a) -> Int -> (a -> a)
--church f n = if n > 0 then f (church f (n-1)) else f
church _ 0 = id
church f n = compose f (church f (n-1))

--curried fact
fact 0 acc = acc
fact x acc = fact (x-1) (x*acc)
--uncurried fact
fact' (0, acc) = acc
fact' (x, acc) = fact' (x-1, x*acc)
--fact with church
update (x, acc) = (x-1, x*acc)
fact'' n = snd $ (church update n) (n, 1)

delete x xs = filter (/= x) xs

--delete duplicates
nub[] = []
nub (x:xs) = x : nub (delete x xs)


--intersect' :: Eq a => [a] -> [a] -> [a]
intersect' xs ys = [ x | x <- xs, y <- ys, x == y ]

intersect'' xs ys = [ x | x <- xs, x `elem` ys ]

setDifference xs ys = [ x | x <- xs, not $ x `elem` ys]

--2^n
powerset [] = [[]]
powerset (x:xs) = half ++ map (x :) half
    where half = powerset xs

-- n!
--"abc"
--abc, acb, bca, bac, cab, cba
permutations [] = [[]]
permutations xs = [ x : ps | x <- xs, ps <- permutations (delete x xs) ]


data BTree a = Leaf a | Fork (BTree a) (BTree a) deriving (Show, Eq)

foo = Fork (Fork (Fork (Fork (Leaf 'a') (Leaf 'b')) (Leaf 'c')) (Leaf 'd')) (Leaf 'e')

size :: BTree a -> Int
size (Leaf _) = 1
size (Fork xt yt) = size xt + size yt

height :: BTree a -> Int
height (Leaf _) = 0
height (Fork xt yt) = 1 + max (height xt) (height yt)

flatten :: BTree a -> [a]
flatten (Leaf x) = [x]
flatten (Fork xt yt) = flatten xt ++ flatten yt

makeBTree :: [a] -> BTree a
makeBTree [x] = (Leaf x)
makeBTree xs = Fork (makeBTree (take m xs)) (makeBTree (drop m xs))
    where m = div (length xs) 2

bar = makeBTree [1..4]

mapBTree :: (a -> b) -> BTree a -> BTree b
mapBTree f (Leaf x) = (Leaf (f x))
mapBTree f (Fork xt yt) = Fork (mapBTree f xt) (mapBTree f yt)

foldBTree :: (a -> b) -> (b -> b -> b) -> BTree a -> b
foldBTree f _ (Leaf x) = f x
foldBTree f g (Fork xt yt) = g (foldBTree f g xt) (foldBTree f g yt)

const' k = \x -> k
f # g = \x y -> f (g x y)

heightFold = foldBTree (const' 0) (succ # max)
flatFold = foldBTree (\x -> [x]) (++)
flatFold' = foldBTree (: []) (++)
sizeFold = foldBTree (const' 1) (+)
mapBTreeFold f = foldBTree (\x -> (Leaf (f x))) Fork
mapBTreeFold' f = foldBTree (Leaf . f) Fork


data RoseTree a = Node a [RoseTree a] deriving (Show, Eq)

m = Node 1 [Node 2 [], Node 3 [Node 4 [], Node 5[]]]

fringe :: RoseTree a -> [a]
fringe (Node x []) = [x]
fringe (Node x xs) = concat (map fringe xs)

flattenRoseTree :: RoseTree a -> [a]
flattenRoseTree (Node x []) = [x]
flattenRoseTree (Node x xs) = x : concat (map flattenRoseTree xs)

sumRoseTree :: Num a => RoseTree a -> a
sunRoseTree (Node x []) = x
sumRoseTree (Node x xs) = x + sum (map sumRoseTree xs)

foldRoseTree f _ (Node x []) = f x
foldRoseTree f g (Node x xs) = g x (map (foldRoseTree f g) xs)

mapRoseTree f = foldRoseTree ((`Node` []) . f) (\x y -> Node (f x) y)