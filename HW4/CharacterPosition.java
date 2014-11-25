/* The data structure of the position of each character in all input file.
 */
package HW4;

public class CharacterPosition
{
	public int fileID;
	public int row;
	public int column;
	
	public CharacterPosition( int fileID, int row, int column )
	{
		this.fileID = fileID;
		this.row = row;
		this.column = column;
	}
}
