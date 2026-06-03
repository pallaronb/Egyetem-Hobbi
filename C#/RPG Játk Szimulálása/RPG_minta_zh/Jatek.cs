using RPG_minta_zh;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TextFile;

namespace RPG_5
{
    class Jatek
    {
        private List<Karakter> parti;
        private List<Ellenfel> ellenfelek;
        public (string,List<string>) Lejatszik() 
        {
            while (parti.Count != 0 || ellenfelek.Count != 0) 
            {
                Kor();
            }
            if (parti.Count != 0)
            {
                List<string> list = new List<string>();
                string eredmeny = "Nyert";
                for (int i = 0; i < parti.Count; i++)
                {
                    list.Add(parti[i].Nev);
                }
                return (eredmeny, list);
            }
            return ("vesztett",null);
        }
        private void Kor() 
        {
            foreach (Karakter k in parti) 
            {
                k.Tamad(parti, ellenfelek);
            }
            HPSzuresE();
            if (ellenfelek.Count == 0)
            {
                return;
            }
            ellenfelek[0].Tamad(parti);
            HPSzuresP();
        }
        private void HPSzuresE() 
        {
            List<Ellenfel> ujEllenfel= new List<Ellenfel>();
            foreach (Ellenfel el in ellenfelek) 
            {
                if (el.EletbenVan()) 
                {
                    ujEllenfel.Add(el);
                }
            }
            ellenfelek = ujEllenfel;
        }
        private void HPSzuresP() 
        {
            List<Karakter> ujParti = new List<Karakter>();
            foreach (Karakter el in parti)
            {
                if (el.EletbenVan())
                {
                    ujParti.Add(el);
                }
            }
            parti = ujParti;
        }
        public void Beolvas(string partiFileName, string ellenfelekFileName)
        {
            parti.Clear();
            ellenfelek.Clear();
            
            TextFileReader reader = new(partiFileName);
            char[] separators = { ' ', '\t' };
            string[] tokens;
            while (reader.ReadLine(out string line))
            {
                tokens = line.Split(separators, StringSplitOptions.RemoveEmptyEntries);
                Karakter? ujKarakter = null;
                switch(tokens[1])
                {
                    case "harcos":
                        ujKarakter = new Harcos(tokens[0], int.Parse(tokens[2]), int.Parse(tokens[3]), int.Parse(tokens[4]), int.Parse(tokens[5]));
                        break;
                    case "kósza":
                        ujKarakter = new Kosza(tokens[0], int.Parse(tokens[2]), int.Parse(tokens[3]), int.Parse(tokens[4]), int.Parse(tokens[5]));
                        break;
                    case "varázsló":
                        ujKarakter = new Varazslo(tokens[0], int.Parse(tokens[2]), int.Parse(tokens[3]), int.Parse(tokens[4]), int.Parse(tokens[5]));
                        break;
                    default: break;
                }

                parti.Add(ujKarakter);
            }

            reader = new(ellenfelekFileName);
            while (reader.ReadLine(out string line))
            {
                tokens = line.Split(separators, StringSplitOptions.RemoveEmptyEntries);
                Ellenfel? ujEllenfel = null;
                switch(tokens[0])
                {
                    case "ork":
                        ujEllenfel = new Ork(int.Parse(tokens[1]), int.Parse(tokens[2]), int.Parse(tokens[3]));
                        break;
                    case "kobold":
                        ujEllenfel = new Kobold(int.Parse(tokens[1]), int.Parse(tokens[2]), int.Parse(tokens[3]));
                        break;
                    case "troll":
                        ujEllenfel = new Troll(int.Parse(tokens[1]), int.Parse(tokens[2]), int.Parse(tokens[3]));
                        break;
                    default:
                        break;
                }
                ellenfelek.Add(ujEllenfel);
            }

        }
    } 
}
