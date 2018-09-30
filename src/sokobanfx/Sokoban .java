package sokobanfx;
import java.util.ArrayList;
public class Sokoban {
    static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
}
class Map{
	static private final int FREE = 0, CORNER = 1, INMATE = 2, BRICK = 3, RECEPTACLE = 4;
	int moves;
	ArrayList<Brick> groupBrick =new ArrayList<>();
	ArrayList<Box> groupBox = new ArrayList<>();
	//ArrayList<BoxFlippy> groupBoxFlippy = new ArrayList<>();
	ArrayList<Receptacle> groupReceptacle = new ArrayList<>();
	Guy guy;
	ArrayList<Reel> reel = new ArrayList<>();
	int mark;
    Map(String[] data){ 
       setLevel(data);
    }
	 void setLevel(String[] data){
		 reel.clear();
		 groupBrick.clear();
		 groupBox.clear();
		 groupReceptacle.clear();
		 for (int i=0; i<data.length;i++)
           for(int j=0; j<data[0].length();j++)
               switch (data[i].charAt(j)){
                   case 'B':
                       groupBrick.add(new Brick(j,i));
                       break;
                   case 'X':
                       groupBox.add(new Box(j,i));
                       break;
						case 'f':
                       groupBox.add(new Box(j,i,true));
							  break;
						case 'F':
                       groupBox.add(new Box(j,i,true));
							  groupReceptacle.add(new Receptacle(j,i));
                       break;
                   case 'R':
                       groupReceptacle.add(new Receptacle(j,i));
                       break;
                   case 'G':
                       guy = new Guy (j,i);
                       break;
                   case 'E':
                       groupBox.add(new Box(j,i));
                       groupReceptacle.add(new Receptacle(j,i));
                       break;
               }
		 System.out.println(guy.x+" "+guy.y);
		 restartOrBuild();
	 }
	  void restart(){
        //verificar referencias
        if (mark>-1)
        {
        System.out.println("restart");
        groupBrick.clear();
        groupBox.clear();
        groupReceptacle.clear();
        final int Size = 0;
		  
        for(int i=0; i < reel.get(Size).groupBrick.size(); i++ )
          groupBrick.add(new Brick(reel.get(Size).groupBrick.get(i).x, reel.get(Size).groupBrick.get(i).y));
        
        for(int i=0; i < reel.get(Size).groupBox.size(); i++ )
          groupBox.add(new Box(reel.get(Size).groupBox.get(i).x, reel.get(Size).groupBox.get(i).y, reel.get(Size).groupBox.get(i).isSlippy));
        
        for(int i=0; i < reel.get(Size).groupReceptacle.size(); i++ )
          groupReceptacle.add(new Receptacle(reel.get(Size).groupReceptacle.get(i).x, reel.get(Size).groupReceptacle.get(i).y));
        guy = new Guy (reel.get(Size).guy.x,reel.get(Size).guy.y,reel.get(Size).guy.orientation);
        reel.clear();
		  restartOrBuild();
        }
    }
	 private void restartOrBuild(){
		 emboned();
		 reel.add(new Reel(guy,groupBox,groupReceptacle,groupBrick));
		 mark = -1;
		 moves = 0;
	 }
	 
    boolean move (int direction){
		Guy guyTemp = new Guy(guy.x,guy.y);
      guyTemp.move(direction);
		//borrar esto para quitar perinola
		guy.orientation = direction;
		for (int i = 0; i< groupBrick.size(); i++)
			if( groupBrick.get(i).x == guyTemp.x && groupBrick.get(i).y== guyTemp.y){
				System.out.println("CASO PARED");
				guy.push = false;
				return false;
			}
        for (int i = 0; i< groupBox.size(); i++)
            if( groupBox.get(i).x == guyTemp.x && groupBox.get(i).y == guyTemp.y){
                Box tempBox = new Box (groupBox.get(i).x,groupBox.get(i).y);
                    tempBox.move(direction);
                    for (int j = 0; j< groupBox.size(); j++)
                        if( groupBox.get(j).x == tempBox.x && groupBox.get(j).y== tempBox.y)
                        {
                            System.out.println("CASO CAJA-CAJA");
									 guy.push = false;
                            return false;
                        }
                    for (int j = 0; j< groupBrick.size(); j++)
                        if( groupBrick.get(j).x == tempBox.x && groupBrick.get(j).y== tempBox.y)
                        {
                            System.out.println("CAJA-PARED");
									 guy.push = false;
                            return false;
                        }
            }
                
         for (int i = 0; i< groupBox.size(); i++)
            if( groupBox.get(i).x == guyTemp.x && groupBox.get(i).y== guyTemp.y){
               for(int j = reel.size()-1; j > mark + 1; j-- )
                reel.remove(j);
               
					 
					 if (!groupBox.get(i).isSlippy){
                groupBox.get(i).move(direction);
					  guy.move(direction);
					 }
					 else{
						 Box tempBox = new Box (groupBox.get(i).x,groupBox.get(i).y);
						 int j = 0;
						 tempBox.move(direction);
						 while (!contains(tempBox)){
							tempBox.move(direction);
							j++;
						 }
						 //revisar
						 for (int k = 0; k<j;k++)
							 groupBox.get(i).move(direction);
							 
					 }
					 reel.add(new Reel(guy,groupBox,groupReceptacle,groupBrick));
                mark++;
                emboned();  //actualiza el status del recepataculo a cada movimiento
                for(int j=0 ; j< groupReceptacle.size();j++)
                    if(groupReceptacle.get(j).status)
                System.out.print("MUEVE-CAJA\n");
					 guy.push = true;
					 moves++;
                return true;
            }
         for(int j = reel.size()-1; j > mark +1; j-- )
         reel.remove(j);
         guy.move(direction);
			reel.add(new Reel(guy,groupBox,groupReceptacle,groupBrick));
         mark++;
         System.out.print("MUEVE-GUY\n");
			guy.push = false;
			moves++;
         return true;
    }
	
    private void emboned(){
        for (int j=0; j < groupReceptacle.size(); j++){
				groupReceptacle.get(j).status = false;
            for(int i=0; i <  groupBox.size(); i++ )
                if(groupReceptacle.get(j).x == groupBox.get(i).x && groupReceptacle.get(j).y ==groupBox.get(i).y)
                    groupReceptacle.get(j).status = true;
		  }
    }
    boolean win(){
        for (int i=0; i < groupReceptacle.size(); i++)
            if(!(groupReceptacle.get(i).status))
                return false;
		  System.out.print("wind\n");
        return true;
    }
    void undo(){
        //verificar referencias
         if (mark > -1)
        {
        System.out.println("undo");
        groupBrick.clear();
        groupBox.clear();
        groupReceptacle.clear();
        final int Size = mark;  
        for(int i=0; i < reel.get(Size).groupBrick.size(); i++ )
          groupBrick.add(new Brick(reel.get(Size).groupBrick.get(i).x, reel.get(Size).groupBrick.get(i).y));
        
        for(int i=0; i < reel.get(Size).groupBox.size(); i++ )
          groupBox.add(new Box(reel.get(Size).groupBox.get(i).x, reel.get(Size).groupBox.get(i).y,reel.get(Size).groupBox.get(i).isSlippy));
        
        for(int i=0; i < reel.get(Size).groupReceptacle.size(); i++ )
          groupReceptacle.add(new Receptacle(reel.get(Size).groupReceptacle.get(i).x, reel.get(Size).groupReceptacle.get(i).y));
		  
         guy = new Guy (reel.get(Size).guy.x,reel.get(Size).guy.y,reel.get(Size).guy.orientation);
         mark--;
			emboned();
			moves--;
        }
    }
    void redo(){
        if (mark < reel.size()-2 )
        {
        System.out.println("redo");
        groupBrick.clear();
        groupBox.clear();
        groupReceptacle.clear();
        final int Size = mark+2;  
        for(int i=0; i < reel.get(Size).groupBrick.size(); i++ )
          groupBrick.add(new Brick(reel.get(Size).groupBrick.get(i).x, reel.get(Size).groupBrick.get(i).y));
        
        for(int i=0; i < reel.get(Size).groupBox.size(); i++ )
         groupBox.add(new Box(reel.get(Size).groupBox.get(i).x, reel.get(Size).groupBox.get(i).y,reel.get(Size).groupBox.get(i).isSlippy));
        
        for(int i=0; i < reel.get(Size).groupReceptacle.size(); i++ )
          groupReceptacle.add(new Receptacle(reel.get(Size).groupReceptacle.get(i).x, reel.get(Size).groupReceptacle.get(i).y));
          guy = new Guy (reel.get(Size).guy.x,reel.get(Size).guy.y,reel.get(Size).guy.orientation);
		mark++;
		emboned();
		moves++;
        }
	
    }
    
	 public  boolean isEmbonated(Box box){
		 for(int i=0; i < groupReceptacle.size();i++)
			 if(box.x == groupReceptacle.get(i).x && box.y == groupReceptacle.get(i).y)
				 return true;
		 return false;
	 }
	  ArrayList<Brick> allFloors(){
		  ArrayList<Brick> adjacents = new ArrayList<>();
		  ArrayList<Brick> openList = new ArrayList<>();
		  ArrayList<Brick> closeList = new ArrayList<>();
		  Brick current = new Brick();
		  openList.add(guy);
		  do{
			  closeList.add(openList.get(0));
			  current = new Brick(openList.get(0));
			  //System.out.println("cURRENT");
			  //current.print();
			  openList.remove(0);
			  //System.out.println("OL");
			  //print(openList);
			  adjacents = allAdjacents(closeList, current);
			  for(int i = 0; i < adjacents.size(); i++)
					if(!contain(openList, adjacents.get(i)))
						openList.add(adjacents.get(i));
		  }while (!openList.isEmpty());
		  return closeList;
	 }
	  ArrayList<Brick> allAdjacents(ArrayList<Brick> list,Brick brick){
		   ArrayList<Brick> temp = new ArrayList<>();
		  if(noCrash(groupBrick, brick.projec(0, 1)) && !contain(list, brick.projec(0, 1)))
					 temp.add(brick.projec(0, 1));
		  if(noCrash(groupBrick, brick.projec(1, 0)) && !contain(list, brick.projec(1, 0)))
					 temp.add(brick.projec(1, 0));
		  if(noCrash(groupBrick, brick.projec(0, -1)) && !contain(list, brick.projec(0, -1)))
					 temp.add(brick.projec(0, -1));
		  if(noCrash(groupBrick, brick.projec(-1, 0)) && !contain(list, brick.projec(-1, 0)))
					 temp.add(brick.projec(-1, 0));
		  return temp;			
	  }
	  private boolean contain (ArrayList<Brick> list, Brick brick){
		for(int i = 0; i< list.size(); i++)
		if (  brick.compare(list.get(i)))
				return true;
		return false;
}
	  private boolean noCrash (ArrayList<Brick> list,Brick brick){
		for(int i = 0; i< list.size(); i++)
			if(brick.compare(list.get(i)))
				return false;
		return true;
	}
	  ArrayList<Brick> allDeathSpaces (){
		  ArrayList<Brick> deathFloor = new ArrayList<>();
		  ArrayList<Brick> allFloors = new ArrayList<>();
		  ArrayList<Brick> temp = new ArrayList<>();
		  allFloors = allFloors();
		  for(int i=0; i<allFloors.size(); i++){
			  if(is(allFloors.get(i))==CORNER){
				  deathFloor.add(allFloors.get(i));
				   temp.clear();
				  for (int j=1; true ; j++){
					  if(is(allFloors.get(i).projec(0, j))==BRICK)
						  break;
					  if(is(allFloors.get(i).projec(0, j))==RECEPTACLE)
						  break;
					  if(is(allFloors.get(i).projec(0, j))==FREE)
						  break;
					  if(is(allFloors.get(i).projec(0, j))==INMATE)
						  temp.add(allFloors.get(i).projec(0, j));
					if (is(allFloors.get(i).projec(0, j))==CORNER){
						for (int k= 0; k< temp.size(); k++)
							if (!contain(deathFloor, temp.get(k)))
							deathFloor.add(temp.get(k));
						//System.out.println("Aquí les llego");
						break;
					}
				  }
				  temp.clear();
				  for (int j=-1; true ; j--){
					  if(is(allFloors.get(i).projec(0, j))==BRICK)
						  break;
					  if(is(allFloors.get(i).projec(0, j))==RECEPTACLE)
						  break;
					  if(is(allFloors.get(i).projec(0, j))==FREE)
						  break;
					  if(is(allFloors.get(i).projec(0, j))==INMATE)
						  temp.add(allFloors.get(i).projec(0, j));
					if (is(allFloors.get(i).projec(0, j))==CORNER){
						for (int k= 0; k< temp.size(); k++)
							if (!contain(deathFloor, temp.get(k)))
							deathFloor.add(temp.get(k));
						break;
					}
				  }
				  temp.clear();
				  for (int j=1; true ; j++){
					  if(is(allFloors.get(i).projec(j, 0))==BRICK)
						  break;
					  if(is(allFloors.get(i).projec(j, 0))==RECEPTACLE)
						  break;
					  if(is(allFloors.get(i).projec(j, 0))==FREE)
						  break;
					  if(is(allFloors.get(i).projec(j, 0))==INMATE)
						  temp.add(allFloors.get(i).projec(j, 0));
					if (is(allFloors.get(i).projec(j, 0))==CORNER){
						for (int k= 0; k< temp.size(); k++)
							if (!contain(deathFloor, temp.get(k)))
							deathFloor.add(temp.get(k));
						break;
					}
				  }
				  temp.clear();
				   for (int j=-1; true ; j--){
					  if(is(allFloors.get(i).projec(j, 0))==BRICK)
						  break;
					  if(is(allFloors.get(i).projec(j, 0))==RECEPTACLE)
						  break;
					  if(is(allFloors.get(i).projec(j, 0))==FREE)
						  break;
					  if(is(allFloors.get(i).projec(j, 0))==INMATE)
						  temp.add(allFloors.get(i).projec(j, 0));
					if (is(allFloors.get(i).projec(j, 0))==CORNER){
						for (int k= 0; k< temp.size(); k++)
							if (!contain(deathFloor, temp.get(k)))
							deathFloor.add(temp.get(k));
						break;
					}
				  }
			  }
		  }
		  return deathFloor;
	  }
	  private int is(Brick brick){
		  if (containsBrick(brick)){
			  return BRICK;
		  }
		  for (int i=0; i< groupReceptacle.size(); i++)
			  if (brick.compare(groupReceptacle.get(i))){
				 // brick.print();
				//System.out.println(": IS RECEPTACLE");
				  return RECEPTACLE;
			  }
		  if ((containsBrick(brick.projec(-1, 0)) && containsBrick( brick.projec(0, -1))) || (containsBrick(brick.projec(1, 0)) && containsBrick( brick.projec(0, -1))) || (containsBrick(brick.projec(-1, 0)) && containsBrick( brick.projec(0, 1))) || (containsBrick(brick.projec(1, 0)) && containsBrick( brick.projec(0, 1)))){
			 // brick.print();
			 // System.out.println(": IS CORNRE");
			  return CORNER;
		  }
		  if (containsBrick(brick.projec(-1, 0))||containsBrick(brick.projec(1, 0))||containsBrick(brick.projec(0, -1))||containsBrick(brick.projec(0, 1))){
			 //  brick.print();
			//  System.out.println(": IS INMATE");
			  return INMATE;
		  }
			 // brick.print();
			 // System.out.println(": IS FREE");
		  return FREE;
	  }
	  boolean containsBrick (Brick brick){ //bien
		 for (int i=0; i<groupBrick.size(); i++)
			 if(brick.compare(groupBrick.get(i)))
				 return true;
		 return false;
	 }
	  boolean isAlive(){
		 for(int i=0; i<groupBox.size(); i++)
			 if(!isAlive(groupBox.get(i)))
				 return false;
		 for(int i=0; i<groupBox.size(); i++)
			 if(contain(allDeathSpaces(), groupBox.get(i)))
				 return false;
		 return true;
	 }
	  boolean isAlive(Box box){
		 if(isEmbonated(box))
			 return true;
		 if(contains(box.translate(-1, 0)) && contains(box.translate(0, -1)) && contains(box.translate(-1, -1)))
			 return false;
		 if(contains(box.translate(1, 0)) && contains(box.translate(0, -1)) && contains(box.translate(1, -1)))
			 return false;
		 if(contains(box.translate(-1, 0)) && contains(box.translate(0, 1)) && contains(box.translate(-1, 1)))
			 return false;
		 if(contains(box.translate(1, 0)) && contains(box.translate(0,1)) && contains(box.translate(1, 1)))
			 return false;
		 if(contain(allDeathSpaces(), box))
			 return false;
		 return true;
	 }
	   boolean contains (Brick brick){ //bien
		 for (int i=0; i<groupBrick.size(); i++)
			 if(brick.compare(groupBrick.get(i)))
				 return true;
		 for (int i=0; i<groupBox.size(); i++)
			 if(brick.compare((Brick)groupBox.get(i)))
				 return true;
		 return false;
	 }
	/*	
		boolean contains (Box brick){ //bien
		for (int i=0; i<groupBrick.size(); i++)
			 if(brick.compare(groupBrick.get(i)))
				 return true;
		 for (int i=0; i<groupBrick.size(); i++)
			 if(brick.compare(groupBrick.get(i)))
				 return true;
		int j;
		for (j=0; j<groupBox.size(); j++)
			 if(brick.compare(groupBox.get(j)))
				  break;

		 for (int i=0; i<groupBox.size(); i++){
			 if (i==j)
				 continue;
			 if(brick.compare((Brick)groupBox.get(i)))
				 return true;
		 }
		 return false;
	 }*/
}

class Brick{
    int x,y;
    Brick (int x, int y){
        this.x = x;
        this.y = y;
    }
	 Brick(){
	 }
	 Brick(Brick unref){
		 x = unref.x;
		 y = unref.y;
	 }
	 boolean compare(Brick brick){
		 if(x==brick.x && y == brick.y)
			 return true;
		 return false;
	 }
	 Brick projec(int x, int y){
		 return new Brick(this.x +x, this.y +y);
	 }
	  void print(){
		 System.out.println(x + ", "+ y);
	 }
}
class Box  extends Brick{
	protected boolean isSlippy;
	 	  
	Box (int x, int y, boolean isSlippy){
		super(x,y);
		this.isSlippy = isSlippy;
	}
   Box (int x, int y){
       this(x, y, false);
    }
	 Box(){
		 super();
	 }
	 public Box(Box unref) {
		super(unref);
	}
    void setPosition(int x,int y){
        this.x = x;
        this.y = y;
    }
    void move(int direction){
        switch (direction){
            case Sokoban.UP:
                y--;
                break;
            case Sokoban.DOWN:
                y++;
                break;
            case Sokoban.LEFT:
                x--;
                break;
            case Sokoban.RIGHT:
                x++;
                break;
        }
    }
	 Box translate(int x, int y){
		 return new Box(this.x+x,this.y+y);
	 }
}
/*
class BoxFlippy extends Box{
	int x0;
	int y0;
	BoxFlippy(int x, int y) {
	super(x,y);
	}
	BoxFlippy() {
	super();
	}
	BoxFlippy(BoxFlippy unref) {
	super(unref);
	}
	boolean move(){
		if(x0==x && y == y0)
			return false;
		return true;
	}
	
}*/

class Guy extends Box{
    int orientation;
	 boolean push;
	 Guy(Guy ref){
		 super(ref.x,ref.y);
       this.orientation = ref.orientation;
    }
	 Guy (int x, int y, int orientation, boolean push){
        super(x,y);
		  this.orientation = orientation;
		  this.push = push;
    }
    Guy (int x, int y, int orientation){
        this(x,y,orientation,false);
    }
    Guy(int x, int y){
       this(x,y,Sokoban.DOWN);
    }
	 Guy(){
		 super();
	 }
	 @Override
	 void move(int direction){
		 super.move(direction);
		 orientation = direction;
	 }
}

class Receptacle extends Brick{
    boolean status;
    public Receptacle(int x, int y) {
        super(x, y);
        status = false;
    }

	public Receptacle(Receptacle receptacle) {
		x=receptacle.x;
		y=receptacle.y;
		status=receptacle.status;
	}
	 
}
class Reel {
    ArrayList<Brick> groupBrick = new ArrayList<>();
    ArrayList<Box> groupBox = new ArrayList<>();
    ArrayList<Receptacle> groupReceptacle = new ArrayList<>();
    Guy guy;
    Reel (Guy guy, ArrayList<Box> groupBox, ArrayList<Receptacle> groupReceptacle, ArrayList<Brick> groupBrick ){
        for(int i=0; i < groupBrick.size(); i++ )
            this.groupBrick.add(new Brick(groupBrick.get(i).x,groupBrick.get(i).y));
        for(int i=0; i < groupBox.size(); i++ )
            this.groupBox.add(new Box(groupBox.get(i).x,groupBox.get(i).y, groupBox.get(i).isSlippy));
        for(int i=0; i < groupReceptacle.size(); i++ )
            this.groupReceptacle.add(new Receptacle(groupReceptacle.get(i).x,groupReceptacle.get(i).y));
        this.guy = new Guy(guy.x,guy.y,guy.orientation); 
    }
}