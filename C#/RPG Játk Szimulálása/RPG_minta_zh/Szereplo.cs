using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RPG_minta_zh
{
    public class Szereplo
    {
        public int HP;
        public int vedelem;
        public int sebzes;
        public Szereplo(int HP, int vedelem, int sebzes)
        {
            this.HP = HP;
            this.vedelem = vedelem;
            this.sebzes = sebzes;
        }

        public bool EletbenVan()
        {
            return HP >= 0;
        }
        public void Sebzodik(Szereplo tamado)
        {
            int tenylegesSebzes = tamado.sebzes - vedelem;
            if (tenylegesSebzes > 0)
            {
                HP = HP - tenylegesSebzes;
            }
        }
    }
    public class Karakter : Szereplo 
    {
        public string? Nev;
        public int cooldown;
        public int korVisszavan;
        public Karakter(string nev, int HP, int vedelem, int sebzes, int cooldown) : base(HP, vedelem, sebzes) { this.Nev = nev; this.cooldown = cooldown; }
        public virtual void Tamad(List<Karakter> parti, List<Ellenfel> ellenfelek) { }
        public virtual void Kepesseg(List<Karakter> parti, List<Ellenfel> ellenfelek) { }
        public bool KepessegetHasznalhat() 
        {
            if (korVisszavan > 0) 
            {
                korVisszavan=korVisszavan-1;
                return false;
            }
            korVisszavan = cooldown;
            return true;
        }
    }
    public class Ellenfel : Szereplo
    {
        public virtual void Tamad(List<Karakter> parti) { }
        public Ellenfel(int HP, int vedelem, int sebzes) : base(HP, vedelem, sebzes) { }
    }
    class Ork : Ellenfel
    {
        public Ork(int HP, int vedelem, int sebzes) : base(HP, vedelem, sebzes) { }
        public override void Tamad(List<Karakter> parti)
        {
            parti[0].Sebzodik(this);
        }
    }
    class Kobold : Ellenfel
    {
        public Kobold(int HP, int vedelem, int sebzes) : base(HP, vedelem, sebzes) { }
        public override void Tamad(List<Karakter> parti)
        {
            int gyengeindex = 0;
            int gyenge = int.MaxValue;
            for (int i = 0; i < parti.Count; i++)
            {
                if (parti[i].HP<gyenge)
                {
                    gyenge = parti[i].HP;
                    gyengeindex = i;
                }
            }
            parti[gyengeindex].Sebzodik(this);
        }
        public bool isKobold() 
        {
            return true;
        }
    }
    class Troll : Ellenfel
    {
        public Troll(int HP, int vedelem, int sebzes) : base(HP, vedelem, sebzes) { }
        public override void Tamad(List<Karakter> parti)
        {
            parti[0].Sebzodik(this);
            if (parti.Count>1)
            {
                parti[1].Sebzodik(this);
            }
            if (parti.Count>3)
            {
                parti[2].Sebzodik(this);
                Karakter csere = parti[2];
                parti[2] = parti[0];
                parti[0] = csere;
            }
        }
    }
    public class Harcos : Karakter
    {
        public Harcos(string nev, int HP, int vedelem, int sebzes, int cooldown) : base(nev, HP, vedelem, sebzes, cooldown) { }
        public override void Tamad(List<Karakter> parti, List<Ellenfel> ellenfelek)
        {
            int sajatindex = 0;
            for (int i = 0; i < parti.Count; i++)
            {
                if (parti[i].Nev == this.Nev)
                {
                    sajatindex = i;
                }
            }
            if (sajatindex != 0)
            {
                return;
            }
            ellenfelek[0].Sebzodik(this);
        }
        public override void Kepesseg(List<Karakter> parti, List<Ellenfel> ellenfelek)
        {
            if (!KepessegetHasznalhat()) 
            {
                return;
            }
            int gyengehp = int.MaxValue;
            int gyengeindex = 0;
            for (int i = 0; i < ellenfelek.Count; i++) 
            {
                if (ellenfelek[i].HP<gyengehp)
                {
                    gyengehp = ellenfelek[i].HP;
                    gyengeindex = i;
                }
            }
            Ellenfel csere = ellenfelek[0];
            ellenfelek[0] = ellenfelek[gyengeindex];
            ellenfelek[gyengeindex] = csere;
        }
    }
    public class Kosza : Karakter
    {
        public Kosza(string nev, int HP, int vedelem, int sebzes, int cooldown) : base(nev, HP, vedelem, sebzes, cooldown) { }
        public override void Tamad(List<Karakter> parti, List<Ellenfel> ellenfelek)
        {
            ellenfelek[0].Sebzodik(this);
        }
        public override void Kepesseg(List<Karakter> parti, List<Ellenfel> ellenfelek)
        {
            if (!KepessegetHasznalhat())
            {
                return;
            }
            int regiSebzes = this.sebzes;
            sebzes = 3 * sebzes;
            ellenfelek[0].Sebzodik(this);
            sebzes = regiSebzes;
        }
    }
    public class Varazslo : Karakter
    {
        public Varazslo(string nev, int HP, int vedelem, int sebzes, int cooldown) : base(nev, HP, vedelem, sebzes, cooldown) { }
        public override void Tamad(List<Karakter> parti, List<Ellenfel> ellenfelek)
        {
            ellenfelek[0].Sebzodik(this);
            if (ellenfelek.Count > 1)
            {
                ellenfelek[1].Sebzodik(this);
            }
        }
        public override void Kepesseg(List<Karakter> parti, List<Ellenfel> ellenfelek)
        {
            if (!KepessegetHasznalhat()) 
            {
                return;
            }
            foreach (Ellenfel e in ellenfelek) 
            {
                e.Sebzodik(this);
            }
        }
    }
}
