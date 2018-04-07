#include<stdio.h>
#include<string.h>
#include<stdlib.h>

int main(){
    FILE *output;
    FILE *answer;
    char command[100];
    char output_str[100];
    char answer_str[100];

    printf("initialize output? (y/n)\n");
    scanf("%c", &(command[0]));

    if(command[0] == 'y'){
        system("java MainBst public/10words.txt public/10words.txt > 10words.out");
        printf("10words complete\n");
        system("java MainBst public/1000words.txt public/2000words.txt > 1000words.out");
        printf("1000words complete\n");
        system("java MainBst public/1000words.txt public/2000words2.txt > 1000words2.output_str");
        printf("1000words2 complete\n");
        system("java -Xms1024m -Xmx4096m MainBst public/sawyer.txt public/sawyer.txt > sawyer.out");
        printf("sawyer complete\n");
        system("java -Xms1024m -Xmx4096m MainBst public/sawyer.txt public/mohicans.txt > sawyer-mohicans.out");
        printf("sawyer-mohicans complete\n");
        system("java -Xms1024m -Xmx4096m MainBst public/mohicans.txt public/mohicans.txt > mohicans.out");
        printf("mohicans complete\n");
        system("java -Xms1024m -Xmx4096m MainBst public/mohicans.txt public/sawyer.txt > mohicans-sawyer.out");
        printf("mohicans-sawyer complete\n\n");
    }

    while(1){
        printf("enter filename to check(---.out)\n(enter q to quit)\n>");
        scanf("%s", command);
        if(!strcmp("q", command))
            exit(0);
        sprintf("%s.out", command);
        output = fopen(command, "r");

        sprintf("public/cpu-i5/%s", command);
        answer = fopen(command, "r");
        while(fgets(output_str, 100, output) != NULL){
            fgets(answer_str, 100, answer);
            if(!strcmp(output_str, answer_str)){
                if(output_str[0] != '[')
                    printf("diff: %s", answer_str);
                else puts(output_str);
            }
        }
    }
    fclose(answer);
    fclose(output);

    return 0;
}
