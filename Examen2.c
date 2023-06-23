#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#define Personas 10

struct registro{
	int id;
	char nombre[30];
	int edad;
};

int main(){
	FILE *archivo;
	struct registro registros[10];
	int i=0;
	char edad[10];
	memset(registros,0,sizeof(struct registro)*10);
	archivo = fopen("archivo.txt","w");
	while(i<Personas){
		printf("Ingrese un registro %i : ",i+1);
		fgets(registros[i].nombre,30,stdin);
		registros[i].id = i+1;
		printf("Ingrese la edad %s : ",registros[i].nombre);
		fgets(edad,10,stdin);
		registros[i].edad = atoi(edad);
		fprintf(archivo,"%i\n%s\n%i\n", registros[i].id, registros[i].nombre, registros[i].edad);
		i++;		
	}
	
	fclose(archivo);
}
