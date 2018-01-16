/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015��3��18��	| duanbokan 	| 	create the file                       
 */

package cn.sharelink.intelligentled.country;

import java.util.Comparator;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author duanbokan
 * 
 */

public class CountryComparator implements Comparator<CountrySortModel>
{
	
	@Override
	public int compare(CountrySortModel o1, CountrySortModel o2)
	{
		
		if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#"))
		{
			return -1;
		}
		else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@"))
		{
			return 1;
		}
		else
		{
			return o1.sortLetters.compareTo(o2.sortLetters);
		}
	}
	
}
