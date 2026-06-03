using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace iypfgiEmeltSzinten
{
    internal class Program
    {
        public static G Szumma<H, G>(int e, int u, Func<int, H> f, G kezd, Func<G, H, int, G> add)
        {
            G s = kezd;
            for (int i = e; i <= u; i++)
            {
                s = add(s, f(i), i);
            }
            return s;
        }
        public static int Darab(int e, int u, Func<int, bool> t)
        {
            int db = 0;
            for (int i = e; i <= u; i++)
            {
                if (t(i))
                {
                    db++;
                }
            }
            return db;
        }
        public static (int maxind, H maxert) Max<H>(int e, int u, Func<int, H> f, Func<H, H, bool> isGreater = null)
        {

            bool _isGreater(H a, H b)
            {
                if (a is IComparable<H> aa)
                {
                    return aa.CompareTo(b) > 0;
                }
                else
                {
                    return isGreater != null ? isGreater(a, b) : false;
                }
            }

            H maxert = f(e);
            int maxind = e;
            for (int i = e + 1; i <= u; i++)
            {
                if (_isGreater(f(i), maxert))
                {
                    maxert = f(i);
                    maxind = i;
                }
            }
            return (maxind, maxert);
        }
        public static H[] Kivalogat<H>(int e, int u, Func<int, bool> t, Func<int, H> f)
        {
            return Szumma(e, u, f, new List<H>(), (s, p, i) => {
                if (t(i))
                {
                    s.Add(p);
                }
                return s;
            }).ToArray();
        }
        public static int Napi(int x, int n,int m, int[,] matrix, int maxert) 
        {
            int db=Darab(0, n-1, row => matrix[row,x] == maxert);
            return db;
        }
        static void Main(string[] args)
        {
            string[] splitted=Console.ReadLine().Split(' ');
            int.TryParse(splitted[0], out int n);
            int.TryParse(splitted[1], out int m);
            int[,] matrix = new int[n, m];
            for (int i = 0; i < n; i++)
            {
                string[] splat=Console.ReadLine().Split(' ');
                for (int j = 0; j <m; j++)
                {
                    int.TryParse(splat[j], out matrix[i, j]);
                }
            }
            var maxResult = Max(0, m * n - 1, index => matrix[index/ m, index %m]);
            int maxert = maxResult.maxert;
            int db = 0;
            db = Darab(0, m - 1, index => Napi(index, n, m, matrix, maxert) > 0);
            int[] y = Kivalogat(0, m - 1, index => Napi(index, n, m, matrix, maxert) > 0, index => index + 1);
            Console.Write(db+" ");
            for (int i = 0; i < db; i++)
            {
                Console.Write(y[i]+" ");
            }
            Console.ReadKey();
        }
    }
}