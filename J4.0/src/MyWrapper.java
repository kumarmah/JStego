
public class MyWrapper {
int freccount=0;
int density=0;
double hratio=0;
double vratio=0;
int qtable=0;
double entropy=0.0;
double histentropy=0.0;
public MyWrapper(int a, int b, int c, int d,int qtable, double entropy, double histentropy){
	freccount=a;
	density=b;
	hratio=c;
	vratio=d;
	this.qtable=qtable;
	this.entropy=entropy;
	this.histentropy=histentropy;
	
	
}
public MyWrapper(){
	freccount=0;
	density=0;
	hratio=0;
	vratio=0;
	this.qtable=0;
	this.entropy=0.0;
	this.histentropy=0.0;

	
}
}
