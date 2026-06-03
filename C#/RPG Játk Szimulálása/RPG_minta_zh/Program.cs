
namespace RPG_5
{
    class Program
    {
        public static void Main(string[] args)
        {
            Jatek jatek = new();
            jatek.Beolvas(args[0], args[1]);
            string eredmeny;
            List<string> tulelok;
            (eredmeny, tulelok) = jatek.Lejatszik();
            Console.WriteLine(eredmeny);
            if (tulelok.Count > 0)
            {
                Console.Write(tulelok[0]);
                for (int i = 1; i < tulelok.Count; i++)
                {
                    Console.Write($", {tulelok[i]}");
                }
                Console.WriteLine();
            }
        }
    }
}