
import Data.List

-- Fill in your name and net ID below
 -- replacing Last with your last name(s)
 -- replacing First with your first name(s)
 -- and replacing netID@unm.edu with your unm email address
studentName = ["Evans", "Tanner"]
netID = "tannerjevans@unm.edu"

-- If you accept the academic honesty pledge, replace the "" below with
     -- "I accept and agree to the academic honesty pledge"
academicHonestyPledge = "I accept and agree to the academic honesty pledge"


-- Fill out the following defintions in wichever order you choose
 -- your final submission should evaluate without errors.
-- Helpers MUST be defined using where or let syntax
-- Where type signatures are defined, you CAN NOT change them
-- You may use any functions in Prelude or Data.List (imported)



-- ================= Breakfast ================= 
duplicates :: Eq a => [a] -> Bool
duplicates [x] = False;
duplicates (x:xs) = if x `elem` xs then True else duplicates xs

duplicatesTests = [
                     not $ duplicates [1..10]
                    ,duplicates "ABCAD"
                    ,not $ duplicates "BAD"
                    ,duplicates [1,2,1]
                  ]


-- ============= Second Breakfast ============== 
gaps :: (Enum t, Eq t) => [t] -> Bool
gaps ls@(x:xs) = or [ not (p == pred q) | (p,q) <-(zip ls xs)]

gapsTests = [
              not $ gaps [1..10]
             ,not $ gaps "ABCD"
             ,gaps "ABD"
             ,gaps [1,2,3,5,6]
             ,gaps "ABBC"
            ]

-- ============= Lists of Lists ============== 
data Lulz a = Lulz [[a]] deriving (Show, Eq)
lulz0 = Lulz [[1,2],[3,4]]
lulz1 = Lulz [[4,3],[2,1]]
lulz2 = Lulz [[4,3],[8,9],[2,1]]

mapLulz :: (a -> b) -> Lulz a -> Lulz b
mapLulz f (Lulz ls) = (Lulz (loop f ls))
     where loop f []     = []
           loop f (x:xs) = map f x : loop f xs

mapLulzTests = [mapLulz even lulz0 == Lulz [[False,True],[False,True]]]

zipLulz :: Lulz a -> Lulz b -> Lulz (a, b)
zipLulz (Lulz xs) (Lulz ys) = (Lulz (loop xs ys))
     where loop []     ys     = []
           loop xs     []     = []
           loop (x:xs) (y:ys) = (pairUp x y) : loop xs ys
           pairUp []     ys     = []
           pairUp xs     []     = []
           pairUp (x:xs) (y:ys) = (x,y) : pairUp xs ys 

zipLulzTests = [
                 zipLulz lulz0 lulz1 == Lulz [[(1,4),(2,3)],[(3,2),(4,1)]]
                ,zipLulz lulz0 lulz2 == Lulz [[(1,4),(2,3)],[(3,8),(4,9)]]
                ,zipLulz lulz2 lulz0 == Lulz [[(4,1),(3,2)],[(8,3),(9,4)]]
                ,zipLulz lulz2 lulz2 == Lulz [[(4,4),(3,3)],[(8,8),(9,9)],[(2,2),(1,1)]]
                ,zipLulz (Lulz ["testing", "strings"]) (Lulz [zip [1..5] ['a'..], zip [2,4..10] ['b','d'..]]) == Lulz [[('t',(1,'a')),('e',(2,'b')),('s',(3,'c')),('t',(4,'d')),('i',(5,'e'))],[('s',(2,'b')),('t',(4,'d')),('r',(6,'f')),('i',(8,'h')),('n',(10,'j'))]]
                ]

zipWithLulz :: (a -> b -> c) -> Lulz a -> Lulz b -> Lulz c
zipWithLulz f (Lulz xs) (Lulz ys) = (Lulz (loop xs ys))
     where loop []     ys     = []
           loop xs     []     = []
           loop (x:xs) (y:ys) = (applyTo x y) : loop xs ys
           applyTo []     ys     = []
           applyTo xs     []     = []
           applyTo (x:xs) (y:ys) = (f x y) : applyTo xs ys 


zipWithLulzTests = [
                     zipWithLulz (+) lulz0 lulz1 == Lulz [[5,5],[5,5]]
                    ,zipWithLulz (+) lulz0 lulz2 == Lulz [[5,5],[11,13]]
                    ,zipWithLulz (+) lulz2 lulz0 == Lulz [[5,5],[11,13]]
                    ,zipWithLulz (+) lulz2 lulz2 == Lulz [[8,6],[16,18],[4,2]]
                    , zipWithLulz (\x y -> x : [snd y]) (Lulz ["testing", "strings"]) (Lulz [zip [1..5] ['a'..], zip [2,4..10] ['b','d'..]]) == Lulz [["ta","eb","sc","td","ie"],["sb","td","rf","ih","nj"]]
                   ]

-- ============ The Final Frontier ============= 
data Tree a = Empty | Leaf a | Fork a (Tree a) (Tree a) deriving (Show, Eq)

bar = Fork 0 (Fork 1 (Fork 2 Empty (Leaf 3)) (Leaf 4)) (Fork 5 Empty (Leaf 6))

mapTree :: (a -> b) -> Tree a -> Tree b
mapTree _ Empty    = Empty
mapTree f (Leaf x) = (Leaf (f x))
mapTree f (Fork z x y) = (Fork (f z) (mapTree f x) (mapTree f y))

mapTreeTests = [mapTree (+ 1) bar == Fork 1 (Fork 2 (Fork 3 Empty (Leaf 4)) (Leaf 5)) (Fork 6 Empty (Leaf 7))]


maxTree :: Ord a => Tree a -> a
maxTree (Leaf x) = x
maxTree (Fork z Empty Empty) = z
maxTree (Fork z x     Empty) = max z (maxTree x)
maxTree (Fork z Empty y)     = max z (maxTree y)
maxTree (Fork z x     y)     = max z (max (maxTree x) (maxTree y))

maxTreeTests = [maxTree bar == 6]


contents :: Tree a -> [a]
contents Empty = []
contents (Leaf x) = [x]
contents (Fork z x y) = [z] ++ contents x ++ contents y

contentsTests = [contents bar == [0,1,2,3,4,5,6]]


interior :: Tree a -> [a]
interior Empty = []
interior (Leaf x) = []
interior (Fork z x y) = [z] ++ interior x ++ interior y

interiorTests = [interior bar == [0,1,2,5]]


frontier :: Tree a -> [a]
frontier Empty = []
frontier (Leaf x) = [x]
frontier (Fork z x y) = frontier x ++ frontier y

frontierTests = [frontier bar == [3,4,6]]


foldTree :: (a -> b) -> (a -> b -> b -> b) -> b -> Tree a -> b
foldTree _ _ h Empty        = h
foldTree f _ _ (Leaf x)     = f x
foldTree f g h (Fork z x y) = g z (foldTree f g h x) (foldTree f g h y)


-- Note that the following problems MUST be defined in terms of foldTree
--  failure to do so will result in 0s for problems defined by other means

mapTreeFold :: (a -> b) -> Tree a -> Tree b
mapTreeFold f = foldTree (Leaf . f) (\z x y -> (Fork (f z) x y)) Empty

mapTreeFoldTests = [mapTreeFold (+ 1) bar == Fork 1 (Fork 2 (Fork 3 Empty (Leaf 4)) (Leaf 5)) (Fork 6 Empty (Leaf 7))]


maxTreeFold :: Ord a => Tree a -> a
maxTreeFold tree = maximum (foldTree (:[]) (\z x y -> [z] ++ x ++ y) [] tree)
--I know that this is a cop out, but I can't think of any other way of resolving the Empty case.

maxTreeFoldTests = [maxTreeFold bar == 6]


contentsFold :: Tree a -> [a]
contentsFold = foldTree (:[]) (\z x y -> [z] ++ x ++ y) []

contentsFoldTests = [contentsFold bar == [0,1,2,3,4,5,6]]


interiorFold :: Tree a -> [a]
interiorFold = foldTree (\x -> []) (\z x y -> [z] ++ x ++ y) []

interiorFoldTests = [interiorFold bar == [0,1,2,5]]


frontierFold :: Tree a -> [a]
frontierFold = foldTree (:[]) (\z x y -> x ++ y) []

frontierFoldTests = [frontierFold bar == [3,4,6]]



tests = [
         ("duplicates",duplicatesTests)
        ,("gaps",gapsTests)
        ,("mapLulz",mapLulzTests)
        ,("zipLulz",zipLulzTests)
        ,("zipWithLulz",zipWithLulzTests)
        ,("mapTree",mapTreeTests)
        ,("maxTree",maxTreeTests)
        ,("contents",contentsTests)
        ,("interior",interiorTests)
        ,("frontier",frontierTests)
        ,("mapTreeFold",mapTreeFoldTests)
        ,("maxTreeFold",maxTreeFoldTests)
        ,("contentsFold",contentsFoldTests)
        ,("interiorFold",interiorFoldTests)
        ,("frontierFold",frontierFoldTests)
        ]
likelyPass = and $ map (and.snd) tests