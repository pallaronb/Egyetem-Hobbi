#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct{
	char location[50];
	char plot[50];
	char type[30];
	int damage;
} Vineyard;

int userMenuChoice(){
	printf("What would you like to do today?\n");
	printf("0 - Read the file \n1 - Add a new entry\n2 - Modify the file\n3 - List Categories\n4 - Delete an entry\n5 - Exit\n");
	int answer;
	scanf("%d", &answer);
	while (getchar() != '\n');
	return answer;
}

void readFile(){
	int f = open("data.txt", O_RDONLY);
	if(f < 0){
		perror("Errorr openning the file!\n");
		return;
	}
	char c;
	while(read(f, &c, sizeof(c))){
		if(c == ','){
			printf("%c ",c);
		}else{
			printf("%c", c);
		}
	}
	printf("\n\n");
	close(f);
}

void addNewEntry(){
	Vineyard v;
	printf("Current file:\n");
	readFile();
	char s[100];
	int w = open("data.txt",O_WRONLY | O_APPEND, S_IRUSR | S_IWUSR);
	if(w < 0){
		printf("Error while openning the file!");
		return;
	}
	printf("(Press ENTER or -1 on an empty line to save and exit)\n");
	while(1){
		printf("Location: ");
		fgets(v.location, 50, stdin);
		if(strcmp(v.location, "\n") == 0){
			printf("Exiting...\n");
			sleep(1);
			break;
		}
		v.location[strcspn(v.location, "\n")] = 0;
		printf("Plot: ");
		fgets(v.plot, 50, stdin);
		if(strcmp(v.plot, "\n") == 0){
			printf("Exiting...\n");
			sleep(1);
			break;
		}
		v.plot[strcspn(v.plot, "\n")] = 0;
		printf("Type: ");
		fgets(v.type, 30, stdin);
		if(strcmp(v.type, "\n") == 0){
			printf("Exiting...\n");
			sleep(1);
			break;
		}
		v.type[strcspn(v.type, "\n")] = 0;
		printf("Damage percentage (0-100): ");
		scanf("%d", &v.damage);
		if(v.damage == -1){
			printf("Exiting...\n");
			sleep(1);
			break;
		}
		while(getchar() != '\n');
		FILE *f = fopen("data.txt", "a");
		fprintf(f, "%s,%s,%s,%d%%\n",v.location, v.plot, v.type, v.damage);
		fclose(f);
	}
	close(w);
	return;
}

void readWithNums(){
	sleep(1);
	int i = 1;
	int f = open("data.txt", O_RDONLY);
	if(f < 0){
		perror("Errorr openning the file!\n");
		return;
	}
	char c;
	int firstCharOfLine = 1;
	while(read(f, &c, sizeof(c))){
		if(firstCharOfLine){
			printf("%d: ", i);
			i += 1;
			firstCharOfLine = 0;
		}
		if(c == ','){
			printf("%c ", c);
		}
		else{
			printf("%c", c);
		}
		if(c == '\n'){
			firstCharOfLine = 1;
		}
	}
	printf("\n");
	close(f);
}

void modifyFile(){
	printf("Current entries:\n");
	readWithNums();
	printf("(Type 0 to go back)\n");
	printf("Which entry would you like to modify?\n");
	int entryNum;
	scanf("%d",&entryNum);
	if(entryNum == 0) return;
	while(getchar() != '\n');
	FILE *oldFile = fopen("data.txt", "r");
	FILE *tempFile = fopen("temp.txt", "w");
	if(!oldFile || !tempFile){
		perror("File error!");
		return;
	}
	char buffer[100];
	int currentLine = 1;
	char newData[100];
	printf("(Press ENTER on an empty line to go back)\n");
	printf("(Please do NOT use SPACE in between entries)\n");
	printf("Enter the new data:\n");
	fgets(newData, sizeof(newData), stdin);
	if(strcmp(newData, "\n") == 0){
		return;
	}
	while(fgets(buffer, sizeof(buffer), oldFile)){
		if(currentLine == entryNum){
			fputs(newData, tempFile);
		}else{
			fputs(buffer, tempFile);
		}
		currentLine++;
	}
	fclose(oldFile);
	fclose(tempFile);
	remove("data.txt");
	rename("temp.txt","data.txt");
	printf("Entry modified\n");
}
void listComponents(int cat){
	FILE *f = fopen("data.txt", "r");
	if(!f){
		perror("Failed to open the file!");
		return;
	}
	Vineyard v;
	char line[256];
	while(fgets(line, sizeof(line), f)){
		int fields = sscanf(line, "%[^,],%[^,],%[^,],%d", v.location, v.plot, v.type, &v.damage);
		if(cat == 1){
			printf("%s\n",v.location);
		}else if(cat == 2){
			printf("%s\n", v.type);
		}
	}
}
void deleteEntry(){
	printf("Current entries:\n");
	readWithNums();
	printf("(Type 0 to go back)\n");
	printf("Which entry would you like to delete?\n");
	int entryNum;
	scanf("%d",&entryNum);
	if(entryNum == 0) return;
	while(getchar() != '\n');
	FILE *oldFile = fopen("data.txt", "r");
	FILE *tempFile = fopen("temp.txt", "w");
	if(!oldFile || !tempFile){
		perror("File error!");
		return;
	}
	char buffer[100];
	int currentLine = 1;
	while(fgets(buffer, sizeof(buffer), oldFile)){
		if(currentLine != entryNum){
			fputs(buffer, tempFile);
		}
		currentLine++;
	}
	fclose(oldFile);
	fclose(tempFile);
	remove("data.txt");
	rename("temp.txt","data.txt");
	printf("Entry modified\n");
}

int main(){
	while(true){
		int choice = userMenuChoice();
		switch(choice){
			case 0:
				printf("Openning the file...\n");
				sleep(1);
				readFile();
				break;
			case 1:
				printf("Adding a new entry...\n");
				sleep(1);
				addNewEntry();
				break;
			case 5:
				printf("Exiting...\n");
				sleep(1);
				exit(1);
				break;
			case 2:
				printf("Openning the file...\n");
				sleep(1);
				modifyFile();
				break;
			case 3:
				printf("Which category would you like to see?\n1 - Place of production\n2 - Type of grapes\n");
				int cat;
				scanf("%d", &cat);
				sleep(1);
				listComponents(cat);
				break;
			case 4:
				printf("Deleting entry...\n");
				sleep(1);
				deleteEntry();
				break;
			default:
				printf("Not a valid choice\n");
				sleep(1);
				break;
		}
	}
}