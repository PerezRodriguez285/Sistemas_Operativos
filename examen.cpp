#include <stdio.h>
#include <conio.h>
#include <string.h>
struct registro{
	char nombre[20];
	int matricula;
}est;

int main(){
	char nom[20];
	int cont = 0;
	
	FILE * ptr, *ptr2;
	ptr = fopen("archivo.txt","a+");
	if (ptr == NULL){
		printf("no se pudo leer");
		return; 
	}
	
	ptr2 = fopen("sistemas.txt","a+");
	if (ptr == NULL){
		printf("error");
		return; 
	}

	while(!feof(ptr)){
		fread(&est,sizeof(est),1,ptr);
		fwrite(&est,sizeof(est),1,ptr2);
	}
	
	printf("datos copiados");
	
	fclose(ptr);
	fclose(ptr2);
	
	getch();
	return 0;	
}
