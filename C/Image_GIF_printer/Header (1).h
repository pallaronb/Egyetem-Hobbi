#include <stdio.h>
#define RESET "\033[0m"
#define BG_BLACK "\033[40m"
#define BG_RED "\033[41m"
#define BG_GREEN "\033[42m"
#define BG_YELLOW "\033[43m"
#define BG_BLUE "\033[44m"
#define BG_MAGENTA "\033[45m"
#define BG_CYAN "\033[46m"
#define BG_WHITE "\033[47m"
#define TERMINAL_CLEAR "\033[2J"
#define TERMINAL_HOME "\033[2H"
typedef enum
{
    BLACK,
    RED,
    GREEN,
    YELLOW,
    BLUE,
    MAGENTA,
    CYAN,
    WHITE,
}color;
const char* background_codes[]
= {
    BG_BLACK,
    BG_RED,
    BG_GREEN,
    BG_YELLOW,
    BG_BLUE,
    BG_MAGENTA,
    BG_CYAN,
    BG_WHITE
};
typedef struct 
{
    int row;
    int col;
    color** matrix;
} image;
typedef struct
{
	image* gif;
} Gif;
