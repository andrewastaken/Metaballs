package util;

public class Table
{
	//top edge: 0
	//right edge: 1
	//bottom edge: 2
	//left edge: 3
	//every pair of numbers is a line between midpoints of the edges
	public static final int[][] EDGE_INDICIES = {
			{},             //0       *---0---*
			{3, 2},         //1       |       |
			{1, 2},         //2       3       1
			{3, 1},         //3       |       |
			{0, 1},         //4       *---2---*
			{0, 3, 1, 2},   //5
			{0, 2},         //6
			{0, 3},         //7
			{0, 3},         //8
			{0, 2},			//9
			{0, 1, 3, 2},   //10
			{0, 1}, 		//11
			{1, 3},			//12
			{1, 2},			//13
			{3, 2},			//14
			{} 				//15
		};
	
	//top left: 0
	//top right: 1
	//bottom right: 2
	//bottom right: 3
	public static final int[][] CORNER_INDICIES_FROM_EDGE = {
			{0, 1},         //0            0--------1
			{1, 2},         //1            |        |
			{2, 3},         //2            |        |
			{3, 0},         //3            |        | 
		};                  //             3--------2 
}
