#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Header.h"
#include <unistd.h>

void color_print(color bg_color) 
{
    printf("%s %s", background_codes[bg_color],RESET);
}
int image_print(char *filename, image* img) {
    FILE* file;
    file=fopen(filename, "r");
    if (file==NULL) {
        perror("File opening failed");
        return -1;
    }
    fscanf(file, "%d", &img->col);
    fscanf(file, "%d", &img->row);
    img->matrix = (color**)malloc(img->row * sizeof(color*));
    for (int i = 0; i < img->row; i++) {
        img->matrix[i] = (color*)malloc(img->col * sizeof(color));
    }
    for (int i = 0; i < img->col; i++) {
        for (int j = 0; j < img->row; j++) {
        	
    		int color_code;
    	        fscanf(file,"%d", &color_code);
    	        img->matrix[i][j]=color_code;
        	    if (color_code >= 0 && color_code <= 7) {
        	        color_print(img->matrix[i][j]);
            }
        }
        printf("\n");
    }
    fclose(file);
    for (int i = 0; i < img->row; i++)
    {
        free(img->matrix[i]);
    }
    free(img->matrix);
    return 0;
}
int print_gif(char *filename)
{
	Gif* gif=(Gif*)malloc(sizeof(Gif));
	gif->gif=(image*)malloc(sizeof(image));
	for(int i=0; i<10;i++)
	{
		char file_with_bg[256];
		sprintf(file_with_bg, "%s.bg%d",filename,i);
		image_print(file_with_bg,gif->gif);
		sleep(2);
		system("clear");
	}
	free(gif->gif);
	free(gif);
	return 0;
}

int main() 
{
    char fajl[256];
    printf("Would you like to print an image or a gif? Please type 'gif' or 'image'. ");
    char valasz[256];
    scanf("%s",valasz);
    if(strcmp(valasz,"gif")==0)
    {
    	printf("Please enter a file's name, without it's extension : ");
    	scanf("%s",fajl);
    	print_gif(fajl);
    }
    else
    {
    	printf("Please enter a file's name, without it's extension: ");
    	scanf("%s",fajl);
    	char file_with_txt[260];
    	sprintf(file_with_txt,"%s.txt",fajl);
    	image* img=(image*)malloc(sizeof(image));
    	image_print(file_with_txt, img);
    	free(img);
    }
    return 0;
}
