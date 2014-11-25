/* PageLoader: Load the content from the text file, and then store into the index hashTable.
 * Calculate the page rank each text file, and return the page rank.
 */
package HW4;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class PageLoader
{
	private String[] pagePaths;		/* All paths of the text files */
	private Hashtable< Character, ArrayList<CharacterPosition> > charPos;

	/* Constructor: Store the paths which are user assigned,
	 * and create new hash table to store the information of the content.
	 */
	public PageLoader( String[] filePaths )
	{
		pagePaths = filePaths;
		charPos = new Hashtable< Character, ArrayList<CharacterPosition> >();

	}	// end of constructor PageLoader( String )

	public void loadFiles()
	throws FileNotFoundException, IOException
	{
		for ( int i = 0; i < pagePaths.length; ++i )
			loadContentToHashTable( i );

	}	// end of fileContent() function

	/* Load the content of the file and store the information of the characters
	 * into the hashTable.
	 */
	private void loadContentToHashTable( int fileIndex )
	throws FileNotFoundException, IOException
	{
		BufferedReader reader = new BufferedReader( new FileReader( pagePaths[fileIndex] ) );
		ArrayList< CharacterPosition > tmpList;
		String stringNow;
		int rowNow = 0;
		int columnNow = 0;

		while( ( stringNow = reader.readLine() ) != null )
		{
			++rowNow;
			columnNow = 0;

			if ( !stringNow.isEmpty() )	// Ignore the line contains only new line characters.
				for ( char aChar : stringNow.toCharArray() )
				{
					++columnNow;

					// If the table has already contained the element, get the old arraylist.
					// Otherwise, create a new one.
					if ( charPos.containsKey( aChar ) )
						tmpList = charPos.get( aChar );
					else
						tmpList = new ArrayList< CharacterPosition >();

					tmpList.add( new CharacterPosition( fileIndex, rowNow, columnNow ) );
					charPos.put( aChar, tmpList );
				}
		}

	}	// end of loadFile() function

}	// end of class PageLoader
