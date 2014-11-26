package HW4;

import java.util.Scanner;

public class IKDDhw4
{
	public static void main( String[] args )
	{
		String[] filePaths = { "src/HW4/webpage_data_5/page1.txt",
				"src/HW4/webpage_data_5/page2.txt",
				"src/HW4/webpage_data_5/page3.txt",
				"src/HW4/webpage_data_5/page4.txt",
				"src/HW4/webpage_data_5/page5.txt"
		};
		Scanner keyboard = new Scanner( System.in );
		String searchingPattern = null;

		PageLoader pageLoader = new PageLoader( filePaths );
		try
		{
			pageLoader.loadFiles();
			pageLoader.calculatePageRank();
			System.out.println( "Data loaded!" );

			while ( true )
			{
				System.out.print( "[in] " );
				searchingPattern = keyboard.nextLine();
				if ( searchingPattern.compareTo( "\\exit" ) == 0 )
					break;
				System.out.println( "[out]\nRank\tFilename" );
				pageLoader.find( searchingPattern );
			}
		}
		catch ( Exception e )
		{
			System.out.println( e.toString() );
		}
	}
}