package ex4;
public class Graph {
    int size;
    int taken;
    String[] vertices;
    int [][] neighbours;

    public Graph(int n){
        taken=0;
        size=n;
        vertices=new String[n];
        neighbours=new int[n][n];

        for(int i=0; i<n;i++){
            for(int j=0; j<n; j++){
                neighbours[i][j]=0;
            }
        }
    }

    public int addVertex(String name){
        if(name.isEmpty()) return -1;
        for(int i=0;i<taken;i++){
            if(name.equals(vertices[i])) return i;
        }

        vertices[taken]=name;
        taken++;
        return taken-1;
    }


    public void addEdge(String n1, String n2){
        int i=addVertex(n1);
        int j=addVertex(n2);

        if(i < 0 || i >= size || j < 0 || j >= size) return;
        neighbours[i][j]=1;
        neighbours[j][i]=1;
    }
    private boolean isWell(int i,int j, int k){
        if(neighbours[j][i] != 1) return false;
        if(neighbours[j][k] != 1) return false;
        if(neighbours[i][k] != 1) return false;
        return true;
    }

    public String findTriangle(){
        String dozret=new String();
        boolean found=false;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                for(int k=0;k<size;k++){
                    if(i != j && i != k && j !=k && found == false){
                        if(isWell(i,j,k) == true){
                            dozret=dozret.concat(vertices[i]);
                            dozret=dozret.concat(" ");
                            dozret=dozret.concat(vertices[j]);
                            dozret=dozret.concat(" ");
                            dozret=dozret.concat(vertices[k]);
                            found=true;
                        }
                    }

                }
            }
        }
        if(found == false) dozret="BRAK";
        return dozret;
    }
}

