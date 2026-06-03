#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>
#include <signal.h>

typedef struct{
    char location[50];
    char plot[50];
    char type[30];
    int area;
    int damage;
} Vineyard;

volatile sig_atomic_t ready_count = 0;
volatile sig_atomic_t done_count = 0;

void parent_sig_handler(int sig){
    if(sig == SIGUSR1){
        ready_count++;
    }
    else if(sig == SIGUSR2){
        done_count++;
    }
}

int userMenuChoice(){
    printf("\nWhat would you like to do today?\n");
    printf("0 - Read the file \n1 - Add a new entry\n2 - Modify the file\n3 - List Categories\n4 - Delete an entry\n5 - Start the purge\n6 - Exit\n");
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
    printf("\n");
    close(f);
}

void addNewEntry(){
    Vineyard v;
    printf("Current file:\n");
    readFile();
    int w = open("data.txt", O_WRONLY | O_APPEND | O_CREAT, S_IRUSR | S_IWUSR);
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
        
        printf("Area (negyszogol): ");
        scanf("%d", &v.area);
        if(v.area == -1){
            printf("Exiting...\n");
            sleep(1);
            break;
        }
        
        printf("Damage percentage (0-100): ");
        scanf("%d", &v.damage);
        if(v.damage == -1){
            printf("Exiting...\n");
            sleep(1);
            break;
        }
        while(getchar() != '\n');
        
        FILE *f = fopen("data.txt", "a");
        fprintf(f, "%s,%s,%s,%d,%d%%\n", v.location, v.plot, v.type, v.area, v.damage);
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
        fclose(oldFile);
        fclose(tempFile);
        remove("temp.txt");
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
        if(sscanf(line, "%[^,],%[^,],%[^,],%d négyszögöl, %d%%", v.location, v.plot, v.type, &v.area, &v.damage) >= 5) {
            if(cat == 1){
                printf("%s\n", v.location);
            }else if(cat == 2){
                printf("%s\n", v.type);
            }
        }
    }
    fclose(f);
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
    printf("Entry deleted\n");
}

void defend(int pipe_defend[2], int pipe_destroy[2], pid_t parent_pid) {
    close(pipe_defend[1]);
    close(pipe_destroy[0]);
    close(pipe_destroy[1]);

    kill(parent_pid, SIGUSR1);

    Vineyard v;
    while(read(pipe_defend[0], &v, sizeof(Vineyard)) > 0) {
        printf("\n[VÉDEKEZÉSI BIZTOS] Tisztelt %s hegyközség, %s, %s tábla főmérnöke! Indul a tavaszi nagy országos védekezés, kérem helyben semmilyen permetezést ne indítsanak. (Kár: %d%%)\n", v.location, v.plot, v.type, v.damage);
        sleep(1);
    }

    close(pipe_defend[0]);

    printf("\nVédekezési biztos befejezte a munkáját!\n");
    sleep(1);
    kill(parent_pid, SIGUSR2);
    exit(0);
}

void destroy(int pipe_defend[2], int pipe_destroy[2], pid_t parent_pid) {
    close(pipe_destroy[1]);
    close(pipe_defend[0]);
    close(pipe_defend[1]);

    kill(parent_pid, SIGUSR1);

    Vineyard v;
    while(read(pipe_destroy[0], &v, sizeof(Vineyard)) > 0) {
        printf("\n[MEGSEMMISÍTŐ] Tisztelt %s hegyközség, %s, %s tábla főmérnöke! Az országos hegybíró utasítására, az aranyszínű sárgaság betegség védekezés keretében, azonnal kérem megsemmisíteni a táblát. (Kár: %d%%)\n", v.location, v.plot, v.type, v.damage);
        sleep(1);
    }

    close(pipe_destroy[0]);

    printf("\nMegsemmisítő biztos befejezte a munkáját\n\n");
    sleep(1);
    kill(parent_pid, SIGUSR2);
    exit(0);
}

void startThePurge() {
    FILE* f = fopen("data.txt", "r");
    if(!f){
        perror("Failed opening the file");
        return;
    }

    char line[256];
    int n = 0;
    while (fgets(line, sizeof(line), f)) n++;

    rewind(f);

    Vineyard* vys = malloc(n * sizeof(Vineyard));

    int q = 0;
    while(fgets(line, sizeof(line), f) && q < n){
        sscanf(line, "%[^,],%[^,],%[^,],%d négyszögöl, %d%%", 
               vys[q].location, vys[q].plot, vys[q].type, &vys[q].area, &vys[q].damage);
        q++;
    }
    fclose(f);

    struct sigaction sa;
    sa.sa_handler = parent_sig_handler;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sigaction(SIGUSR1, &sa, NULL);
    sigaction(SIGUSR2, &sa, NULL);
    ready_count = 0;
    done_count = 0;

    sigset_t mask, oldmask;
    sigemptyset(&mask);
    sigaddset(&mask, SIGUSR1);
    sigaddset(&mask, SIGUSR2);
    sigprocmask(SIG_BLOCK, &mask, &oldmask);

    int pipe_destroy[2];
    int pipe_defend[2];

    if(pipe(pipe_destroy) == -1 || pipe(pipe_defend) == -1){
        perror("Pipe error");
        free(vys);
        return;
    }

    pid_t parent_pid = getpid();

    printf("\nHegybíró várakozik az első biztos jelentkezésére!\n");
    sleep(1);

    pid_t pid_destroy = fork();
    if (pid_destroy == 0) {
        destroy(pipe_defend, pipe_destroy, parent_pid);
    }

    while(ready_count < 1){
        sigsuspend(&oldmask);
    }

    printf("Hegybíró várakozik a második biztos jelentkezésére!\n");
    sleep(1);

    pid_t pid_defend = fork();
    if (pid_defend == 0) {
        defend(pipe_defend, pipe_destroy, parent_pid);
    }

    while(ready_count < 2){
        sigsuspend(&oldmask);
    }

    close(pipe_destroy[0]);
    close(pipe_defend[0]);

    printf("\nMindkét biztos munkára kész, adatok továbbítása!\n");
    sleep(1);

    for(int i = 0; i < n; i++){
        if(vys[i].damage >= 30){
            write(pipe_destroy[1], &vys[i], sizeof(Vineyard));
        } else {
            write(pipe_defend[1], &vys[i], sizeof(Vineyard));
        }
    }
    free(vys);

    printf("\nHegybíró várakozik az első biztos munkájának befejezésére!\n");
    sleep(1);
    close(pipe_destroy[1]);
    
    while(done_count < 1){
        sigsuspend(&oldmask);
    }

    printf("Hegybíró várakozik a második biztos munkájának befejezésére!\n");
    close(pipe_defend[1]);
    
    while(done_count < 2){
        sigsuspend(&oldmask);
    }

    sigprocmask(SIG_SETMASK, &oldmask, NULL);

    printf("\nMindkét biztos befejezte a munkavégzést.\n");

    wait(NULL);
    wait(NULL);

    printf("JELENTÉS A MEZŐGAZDASÁGI MINISZTERNEK:\n");
    printf("Tisztelt Miniszter Úr! A tavaszi munkálatok befejeződtek.\n");
    printf("A fertőzött területek megsemmisítése és az országos\n");
    printf("védekezési program sikeresen lezárult.\n");
    printf("Tisztelettel: Országos Hegybíró\n");
    
    exit(0); 
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
            case 6:
                printf("Exiting...\n");
                sleep(1);
                exit(0);
                break;
            case 5:
                printf("Starting the purge!\n");
                sleep(1);
                startThePurge();
                break;
            default:
                printf("Not a valid input\n");
                break;
        }
    }
}