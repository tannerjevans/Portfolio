import Data.List

studentName = ["Evans", "Tanner"]
netID = "tannerjevans@unm.edu"


-- Problem 1, bits2num
bits2num :: Num a => [Char] -> a
bits2num ls = convert (reverse ls) 1
    where convert [] _ = 0
          convert (x:xs) pow = (if x == '1' then pow else 0) + convert xs (pow * 2)

p1tests = [bits2num "1011000" == 88]


-- Problem 2, num2bits
num2bits :: Integral a => a -> [Char]
num2bits n = reverse $ convert n
    where convert 0 = "0"
          convert num = (if num `rem` 2 == 1 then '1' else '0') : convert (num `div` 2)

p2tests = [num2bits 87783 == "010101011011100111"]


-- Problem 3, variance
variance :: (Num a, Fractional a) => [a] -> a
variance ls = sum [ (x - mean)^2 | x <- ls ] / len
    where len = fromIntegral (length ls)
          mean = (sum ls) / len

p3tests = [variance [1..10] == 8.25]


-- Problem 4, difference
difference :: Eq a => [a] -> [a] -> [a]
difference xs ys = nub [ x | x <- xs, not (x `elem` ys) ]

p4tests = [difference "ABCD" "AD" == "BC",difference "ABCDCBA" "AD" == "BC"]


-- Problem 5, splits
splits ::  Ord a => [a] -> [([a], [a])]
splits ls = split ls (pred (length ls))
    where split :: Ord a => [a] -> Int -> [([a], [a])]
          split _    0 = []
          split list k = [ (x, difference list x) | x <- lop k list ] ++ split list (pred k)
          lop _ []     = []
          lop 1 (x:xs) = [x] : lop 1 xs
          lop n (x:xs) = map (x :) (lop (pred n) xs) ++ lop n xs

p5tests = [sort (splits "abc") == sort [("c","ab"),("b","ac"),("bc","a"),("a","bc"),("ac","b"),("ab","c")]]


-- Problem 6, argmin
argmin ::  (Ord a) => (t -> a) -> [t] -> t
argmin f xs = head [ x | x <- xs, (f x) == minVal ]
    where minVal = minimum (map f xs)

p6tests = [argmin length ["ABC","EF","GHIJ","K"] == "K"]


data Htree a = HLeaf Double a | HFork Double [a] (Htree a) (Htree a) deriving (Show, Eq)
-- Problem 7, bogus

instance (Ord a) => Ord (Htree a) where
    (HLeaf x _)     <  (HLeaf y _)     = x < y
    (HLeaf x _)     <  (HFork y _ _ _) = x < y
    (HFork x _ _ _) <  (HLeaf y _)     = x < y
    (HFork x _ _ _) <  (HFork y _ _ _) = x < y
    (HLeaf x _)     <= (HLeaf y _)     = x <= y
    (HLeaf x _)     <= (HFork y _ _ _) = x <= y
    (HFork x _ _ _) <= (HLeaf y _)     = x <= y
    (HFork x _ _ _) <= (HFork y _ _ _) = x <= y

-- encode character using Huffman coding tree
encode (HFork _ _   (HLeaf _ l)        (HLeaf _ r))      c = if c == l then "0" else "1"
encode (HFork _ _   (HLeaf _ l)      v@(HFork _ rs _ _)) c = if c == l then "0" else '1':(encode v c)
encode (HFork _ _ u@(HFork _ ls _ _) v@(HLeaf _ r))      c = if c == r then "1" else '0':(encode u c)
encode (HFork _ _ u@(HFork _ ls _ _) v@(HFork _ rs _ _)) c = if c `elem` ls then '0':(encode u c) else '1':(encode v c)

-- decode message using Huffman coding tree
decode t [] = []
decode t (x:xs) = loop t (x:xs)
    where loop (HLeaf _ l) xs = l:(decode t xs)
          loop (HFork _ _ u v) ('0':xs) = loop u xs
          loop (HFork _ _ u v) ('1':xs) = loop v xs


merge u@(HLeaf x l)      v@(HLeaf y r)      = HFork (x+y) [l,r] u v
merge u@(HLeaf x l)      v@(HFork y rs _ _) = HFork (x+y) (l:rs) u v
merge u@(HFork x ls _ _) v@(HLeaf y r)      = HFork (x+y) (r:ls) u v
merge u@(HFork x ls _ _) v@(HFork y rs _ _) = HFork (x+y) (ls ++ rs) u v

bogus :: Ord a => [(Double, a)] -> Htree a
bogus [(x, y)] = HLeaf x y
bogus list = merge (bogus (fst splitList)) (bogus (snd splitList))
    where splitList = argmin (\x -> abs (keySum (fst x) - keySum (snd x))) (splits list)
          keySum ls = sum $ map fst ls

p7tests = let xs = [(0.30,'e'), (0.14,'h'), (0.1,'l'), (0.16,'o'), (0.05,'p'), (0.23,'t'), (0.02,'w')] in 
                [(decode (bogus xs) $ concatMap (encode (bogus xs)) "hello") == "hello", 
                 concatMap (encode (bogus xs)) "hello" /= concatMap (encode (bogus xs)) "oellh"]


-- Problem 8, church
church :: Int -> (a -> a) -> a -> a
church n = \f -> foldr (\x y -> (f . y)) id [1..n]

p8tests = [church 4 tail "ABCDEFGH" == "EFGH", church 100 id 9001 == 9001]


data Btree a = BLeaf a | BFork (Btree a) (Btree a) deriving (Show, Eq, Ord)
-- Problem 9, trees

trees :: (Ord t) => [t] -> [Btree t]
trees [x] = [BLeaf x]
trees xs  = [ BFork i j | (x, y) <- splits xs, i <- (trees x), j <- (trees y) ]

p9tests = [(sort $ trees "ABCDE") !! 114 == BFork (BLeaf 'A') (BFork (BFork (BFork (BLeaf 'E') (BLeaf 'B')) (BLeaf 'C')) (BLeaf 'D')),
           length (trees [0..4]) == 1680]


bases = "AGCT"
-- Problem 10, insertions
insertions :: String -> [String]
insertions xs = [ insert' x y  | x <- bases, y <- [0..(length xs)] ]
    where insert' x k = take k xs ++ [x] ++ drop k xs

p10tests = [insertions "GC" == ["AGC","GAC","GCA","GGC","GGC","GCG","CGC","GCC","GCC","TGC","GTC","GCT"]]


-- Problem 11, deletions
deletions :: [a] -> [[a]]
deletions xs = [ delete' x | x <- [1..(length xs)] ]
    where delete' k = take (pred k) xs ++ drop k xs

p11tests = [deletions "AGCT" == ["GCT","ACT","AGT","AGC"]]


-- Problem 12, substitutions
substitutions :: String -> [String]
substitutions xs = [ replace' x y | x <- bases, y <- [1..(length xs)] ]
    where replace' x k = take (pred k) xs ++ [x] ++ drop k xs

p12tests = [substitutions "ACT" == ["ACT","AAT","ACA","GCT","AGT","ACG","CCT","ACT","ACC","TCT","ATT","ACT"]]


-- Problem 13, transpositions
transpositions :: [a] -> [[a]]
transpositions xs = [ take x xs ++ (reverse (take 2 (drop x xs))) ++ drop (2 + x) xs | x <- [0..((length xs) - 2)] ]

p13tests = [transpositions "GATC" == ["AGTC","GTAC","GACT"]]



tests = [p1tests,p2tests,p3tests,p4tests,p5tests,p6tests,p7tests,p8tests,p9tests,p10tests,p11tests,p12tests,p13tests]
likelyCorrect = let results = [and t | t <- tests] in (and results, filter (not.snd) $ zip [1..] results)