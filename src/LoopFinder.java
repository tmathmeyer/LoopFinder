import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoopFinder
{
	public static Map<String, Set<Trade>> trades = new HashMap<>();

	public static void main(String... args)
	{
		makeCombo("1 glowstone -> 4 glowstone-dust : crafting");
		makeCombo("4 glowstone-dust -> 1 glowstone : crafting");
		makeCombo("9 coal -> 1 coal-block : crafting");
		makeCombo("1 coal-block -> 9 coal : crafting");
		makeCombo("9 emerald -> 1 emerald-block : crafting");
		makeCombo("1 emerald-block -> 9 emerald : crafting");
		makeCombo("9 diamond -> 1 diamond-block : crafting");
		makeCombo("1 diamond-block -> 9 diamond : crafting");
		makeCombo("9 iron -> 1 iron-block : crafting");
		makeCombo("1 iron-block -> 9 iron : crafting");
		makeCombo("9 redstone -> 1 redstone-block : crafting");
		makeCombo("1 redstone-block -> 9 redstone : crafting");
		makeCombo("9 gold -> 1 gold-block : crafting");
		makeCombo("1 gold-block -> 9 gold : crafting");
		makeCombo("9 lapis-block -> 1 emerald : crafting");
		makeCombo("9 gold-nugget -> 1 gold : crafting");

		
		
		makeCombo("4 diamond -> 7 emerald : cs-ee");
		makeCombo("8 obsidian -> 64 lapis-block : cs-byr");
		makeCombo("8 quartz -> 32 lapis-block : cs-byr");
		makeCombo("8 glowstone-dust -> 32 lapis-block : cs-byr");
		makeCombo("4 glowstone -> 64 lapis-block : cs-byr");

		makeCombo("1 emerald-ore -> 1 emerald-block : cs-sss");
		makeCombo("1 diamond-ore -> 4 diamond : cs-sss");
		makeCombo("2 iron-ore -> 1 iron-block : cs-sss");
		makeCombo("2 coal-ore -> 1 coal-block : cs-sss");
		makeCombo("2 gold-ore -> 1 gold-block : cs-sss");

		makeCombo("3 gold-block -> 24 emerald : cellar");
		makeCombo("18 emerald -> 1 diamond-block : cellar");
		makeCombo("8 iron-block -> 3 emerald-block : cellar");
		makeCombo("3 emerald-block -> 3 gold-block : cellar");

		makeCombo("8 coal-block -> 40 lapis-block : cellar");
		makeCombo("8 redstone-block -> 42 lapis-block : cellar");
		
		makeCombo("19 lapis-block -> 1 diamond : ender-isles-spelunker");
		makeCombo("9 lapis-block -> 4 mycelium : ender-isles-spelunker");
		makeCombo("5 lapis-block -> 1 iron : ender-isles-spelunker");
		makeCombo("8 lapis-block -> 1 redstone-block : ender-isles-spelunker");
		makeCombo("18 lapis-block -> 1 diamond : ender-isles-spelunker");
		makeCombo("10 lapis-block -> 9 glowstone-dust : ender-isles-spelunker");
		makeCombo("2 lapis-block -> 1 gold-nugget : ender-isles-spelunker");
		
		makeCombo("8 lapis -> 1 redstone : mclovin");
		makeCombo("8 lapis-block -> 1 ghast-tear : mclovin");
		makeCombo("8 lapis-block -> 1 redstone-block : mclovin");

		int counter = 0;
		while ((++counter < 10) && combo("lapis-block") > 0)
			;
	}

	public static int combo(String t)
	{
		Set<Trade> tr = trades.get(t);
		Set<Trade> news = new HashSet<>();
		// Set<String> doNext = new HashSet<>();
		int repls = 0;

		for (Trade tra : tr)
		{
			int fromcount = tra.itemFrom.amount;
			int tocount = tra.itemTo.amount;
			
			Set<Trade> nee = trades.get(tra.itemTo.name);
			if (nee == null) continue;
			
			for (Trade deep : trades.get(tra.itemTo.name))
			{
				int dfc = deep.itemFrom.amount;
				int tfc = deep.itemTo.amount;

				int nf = fromcount * dfc;
				int nt = tocount * tfc;
				
				int x = GCD(nf, nt);
				
				nf /= x;
				nt /= x;

				if (tra.itemFrom.name.equals(deep.itemTo.name))
				{
					if (nf < nt)
					{
						System.out.println("("+tra.itemFrom.name + ": " + nf + ") to ("
						        + deep.itemTo.name + ": " + nt + ") @ " + tra.sellerLocation + " -> " + deep.sellerLocation);
						//System.out.println("THERES A LOOPHOLE WITH " + tra.itemFrom.name);
						return 0;
					}
				} else
				{
					repls++;
					news.add(new Trade(new Item(tra.itemFrom.name, nf), new Item(deep.itemTo.name, nt),
					        tra.sellerLocation + "->" + deep.sellerLocation));
					//System.out.println("making new trade from (" + tra.itemFrom.name + ": " + nf + ") to ("
					//        + deep.itemTo.name + ": " + nt + ") @ " + tra.sellerLocation + "->" + deep.sellerLocation);

				}
			}
		}

		trades.put(t, news);
		return repls;
	}

	public static int GCD(int a, int b)
	{
		if (b == 0)
			return a;
		return GCD(b, a % b);
	}

	// "9 lapis-block -> 81 lapis : cellar"
	public static void makeCombo(String s)
	{
		String[] parts = s.split(" ");
		Set<Trade> set = trades.get(parts[1]);
		if (set == null)
		{
			set = new HashSet<>();
		}

		set.add(new Trade(new Item(parts[1], Integer.parseInt(parts[0])),
		        new Item(parts[4], Integer.parseInt(parts[3])), parts[6]));
		trades.put(parts[1], set);
	}

	static class Item
	{
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + amount;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Item other = (Item) obj;
			if (name == null)
			{
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		final String name;
		final int amount;

		public Item(String n, int a)
		{
			name = n;
			amount = a;
		}

		public Item combine(Item other)
		{
			if (other.name.equals(this.name))
			{
				return new Item(this.name, other.amount + this.amount);
			}
			return null;
		}
	}

	static class Trade
	{
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((itemFrom == null) ? 0 : itemFrom.hashCode());
			result = prime * result + ((itemTo == null) ? 0 : itemTo.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Trade other = (Trade) obj;
			if (itemFrom == null)
			{
				if (other.itemFrom != null)
					return false;
			} else if (!itemFrom.equals(other.itemFrom))
				return false;
			if (itemTo == null)
			{
				if (other.itemTo != null)
					return false;
			} else if (!itemTo.equals(other.itemTo))
				return false;
			if (sellerLocation == null)
			{
				if (other.sellerLocation != null)
					return false;
			} else if (!sellerLocation.equals(other.sellerLocation))
				return false;
			return true;
		}

		public final Item itemFrom, itemTo;
		public final String sellerLocation;

		public Trade(Item f, Item t, String l)
		{
			itemFrom = f;
			itemTo = t;
			sellerLocation = l;
		}
	}

}
