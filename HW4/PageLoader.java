/* PageLoader: Load the content from the text file, and then store into the index hashTable.
 * Calculate the page rank each text file, and return the page rank.
 */
package HW4;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class PageLoader
{
	private String[] pagePaths_;		/* All paths of the text files */
	private Hashtable< Character, ArrayList<CharacterPosition> > charPos_;

	/* Constructor: Store the paths which are user assigned,
	 * and create new hash table to store the information of the content.
	 */
	public PageLoader( String[] filePaths )
	{
		pagePaths_ = filePaths;
		charPos_ = new Hashtable< Character, ArrayList<CharacterPosition> >();

	}	// end of constructor PageLoader( String )

	public void loadFiles()
	throws FileNotFoundException, IOException
	{
		for ( int i = 0; i < pagePaths_.length; ++i )
			loadContentToHashTable( i );

	}	// end of fileContent() function

	/* Load the content of the file and store the information of the characters
	 * into the hashTable.
	 */
	private void loadContentToHashTable( int fileIndex )
	throws FileNotFoundException, IOException
	{
		BufferedReader reader = new BufferedReader( new FileReader( pagePaths_[fileIndex] ) );
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
					if ( charPos_.containsKey( aChar ) )
						tmpList = charPos_.get( aChar );
					else
						tmpList = new ArrayList< CharacterPosition >();

					tmpList.add( new CharacterPosition( fileIndex, rowNow, columnNow ) );
					charPos_.put( aChar, tmpList );
				}
		}

	}	// end of loadFile() function

	/* Search the content of the file.
	 */
	public void find( String searchingPattern )
	{
		Boolean[] isMatched = new Boolean[ pagePaths_.length ];
		for ( int i = 0; i < isMatched.length; ++i )
			isMatched[i] = false;

		isMatched = searchContent( isMatched, searchingPattern );
		for ( Boolean singleMatch : isMatched )
			System.out.println( singleMatch );
	}

	/* Search the content of the file.
	 * Return true, if matched.
	 */
	private Boolean[] searchContent( Boolean[] isMatched, String searchingPattern )
	{
		Stack< CharacterPosition > stack = new Stack< CharacterPosition >();
		ArrayList< CharacterPosition > arrayListSearchingNow;
		int[] arrayListIndex = new int[ searchingPattern.length() ];
		int charSearchingNow = 0;
		Boolean moveToNextOrLastChar = false;
		CharacterPosition characterPositionSearchingNow = null;
		CharacterPosition characterPositionCompare = null;

		// There is only 1 character in the searching pattern.
		// All of elements of the information of the position of this character would be matched.
		if ( searchingPattern.length() == 1 )
		{
			if ( ( arrayListSearchingNow = getArrayListByChar( searchingPattern.charAt(0) ) ) == null )
				return isMatched;

			for ( int i = 0; i < arrayListSearchingNow.size(); ++i )
				isMatched[ arrayListSearchingNow.get(i).fileID ] = true;

			return isMatched;
		}

		while ( charSearchingNow < searchingPattern.length() )
		{
			moveToNextOrLastChar = false;

			if ( charSearchingNow == 0 )
			{
				if ( ( arrayListSearchingNow = getArrayListByChar( searchingPattern.charAt(charSearchingNow) ) ) == null )
					break;

				if ( arrayListIndex[0] == arrayListSearchingNow.size() )
					break;

				characterPositionSearchingNow = arrayListSearchingNow.get( arrayListIndex[0] );
				++arrayListIndex[0];	// Increase the index of the next element of the array list of the first character
				++charSearchingNow;		// Move to the next character of the searching pattern
			}

			// Get the array list of the information of the position of the next character
			if ( ( arrayListSearchingNow = getArrayListByChar( searchingPattern.charAt(charSearchingNow) ) ) == null )
				break;
			// All position of this character is searched, and then terminate the matching.
			if ( arrayListIndex[charSearchingNow] == arrayListSearchingNow.size() )
				break;

			for ( int i = arrayListIndex[ charSearchingNow ]; i < arrayListSearchingNow.size(); ++i )
			{
				arrayListIndex[ charSearchingNow ] = i;

				characterPositionCompare = arrayListSearchingNow.get(i);

				if ( characterPositionSearchingNow.fileID == characterPositionCompare.fileID &&
						characterPositionSearchingNow.row == characterPositionCompare.row &&
						characterPositionSearchingNow.column == characterPositionCompare.column - 1 )
				{
					++arrayListIndex[charSearchingNow];
					++charSearchingNow;
					stack.push( characterPositionSearchingNow );
					characterPositionSearchingNow = characterPositionCompare;
					moveToNextOrLastChar = true;
					break;
				}

				if ( characterPositionSearchingNow.fileID < characterPositionCompare.fileID ||
						( characterPositionSearchingNow.fileID == characterPositionCompare.fileID &&
						characterPositionSearchingNow.row < characterPositionCompare.row ) ||
						( characterPositionSearchingNow.fileID == characterPositionCompare.fileID &&
						characterPositionSearchingNow.row == characterPositionCompare.row &&
						characterPositionSearchingNow.column < characterPositionCompare.column ) )
				{
					--charSearchingNow;
					if ( !stack.isEmpty() )
						characterPositionSearchingNow = stack.pop();
					moveToNextOrLastChar = true;
					break;
				}
			}

			// All characters are matched. Mark the file and reset to match the next pattern.
			if ( charSearchingNow == searchingPattern.length() )
			{
				isMatched[ characterPositionSearchingNow.fileID ] = true;
				charSearchingNow = 0;
			}

			// All position of this character is searched,
			// and there is no matched position.
			if ( !moveToNextOrLastChar )
				break;

		}	// end of while ( charSearchingNow < searchingPattern.length() )

		return isMatched;

	}	// end of searchContent() function

	/* Get the array list of the information of the position of the characters.
	 * If it dosen't contain the character specified, return null.
	 */
	private ArrayList< CharacterPosition > getArrayListByChar( Character character )
	{
		if ( !charPos_.containsKey( character ) )
			return null;
		else
		{
			return charPos_.get( character );
		}
	}

}	// end of class PageLoader
