#include<stdio.h>
#include<string.h>
#include<stdlib.h>

int main(){
    FILE *output;
    FILE *answer;
    char command[100];
    char buf[100];
    char output_str[100];
    char answer_str[100];

    printf("initialize output? (y/n)\n");
    scanf("%c", &(command[0]));

    if(command[0] == 'y'){
        system("java MainBst public/10words.txt public/10words.txt > output/10words.out");
        printf("10words complete\n");
        system("java MainBst public/1000words.txt public/2000words.txt > output/1000words.out");
        printf("1000words complete\n");
        system("java MainBst public/1000words.txt public/2000words2.txt > output/1000words2.out");
        printf("1000words2 complete\n");
        system("java -Xms1024m -Xmx4096m MainBst public/sawyer.txt public/sawyer.txt > output/sawyer.out");
        printf("sawyer complete\n");
        system("java -Xms1024m -Xmx4096m MainBst public/sawyer.txt public/mohicans.txt > output/sawyer-mohicans.out");
        printf("sawyer-mohicans complete\n");
        system("java -Xms1024m -Xmx4096m MainBst public/mohicans.txt public/mohicans.txt > output/mohicans.out");
        printf("mohicans complete\n");
        system("java -Xms1024m -Xmx4096m MainBst public/mohicans.txt public/sawyer.txt > output/mohicans-sawyer.out");
        printf("mohicans-sawyer complete\n\n");
    }
    //i should've used python
    while(1){
        printf("Enter filename to check(---.out)\n(Enter q to quit, ls to show filenames. Right part is your answer)\n>");
        scanf("%s", command);
        if(!strcmp("q", command))
            break;
        if(!strcmp("ls", command)){
            system("ls ./output");
            continue;
        }
        strcat(command, ".out");
        strcpy(buf, "output/");
        strcat(buf, command);
        output = fopen(buf, "r");	

        strcpy(buf, "public/cpu-i5/");
        strcat(buf, command);
        answer = fopen(buf, "r");

        if(answer == NULL || output == NULL){
            printf("cannot open file\n");
            continue;
        }
        int block = 0;
        while(fgets(output_str, 100, output) != NULL){
            fgets(answer_str, 100, answer);
            if(output_str[0] != '['){
                if(block != 0){
                    printf("%d differences found on current tree\n", block);
                    block = 0;
                }
            }
            if(strcmp(output_str, answer_str)){
                strtok(answer_str,"\n");
                if(output_str[0] != '[')
                    printf("difference in statistics:          %-80s %s", answer_str, output_str);
                else{
                    if(block == 0){
                        printf("difference in blocks: %s %s", answer_str, output_str);
                        printf("and so on..\n");
                    }
                    block++;
                }
            }
        }
    
    }

    fclose(answer);
    fclose(output);

    return 0;
}
