{-# LANGUAGE StandaloneDeriving #-}
{-# LANGUAGE UndecidableInstances #-}
module Hazi2 where

import Data.Functor.Classes

data Tree a = Leaf (Maybe a) | Node (Tree a) a (Tree a) deriving (Eq, Show)

instance Functor Tree where
    fmap :: (a -> b ) -> Tree a -> Tree b
    fmap _ (Leaf Nothing) = Leaf Nothing
    fmap f (Leaf (Just x)) = Leaf (Just (f x))
    fmap f (Node l x r) = Node (fmap f l) (f x) (fmap f r)


data Gofri f a
    = MkGofri (f a) (f (Gofri f a))

instance Functor f => Functor (Gofri f) where
    fmap g (MkGofri xs xss) =
        MkGofri
            (fmap g xs)
            (fmap (fmap g) xss)

deriving instance (Eq (f a), Eq (f (Gofri f a))) => Eq (Gofri f a)
deriving instance (Show (f a), Show (f (Gofri f a))) => Show (Gofri f a)


data CrazyType3 a b
    = CrazyCon1 a b a 
    | CrazyCon2 (CrazyType3 a b) [b] [a]
    | CrazyCon3 (CrazyType3 Int b) (CrazyType3 a a) [[b]]
    deriving (Eq, Show)

instance Functor (CrazyType3 a) where
    fmap f (CrazyCon1 x y z) = CrazyCon1 x (f y) z
    fmap f (CrazyCon2 ct bs as) = CrazyCon2 (fmap f ct) (map f bs) as
    fmap f (CrazyCon3 ct1 ct2 bss) = CrazyCon3 (fmap f ct1) ct2 (map (map f) bss)