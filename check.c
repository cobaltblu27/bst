#include<stdio.h>
#include<string.h>

int main(){
    char command[100];
    char answer_str[100];
    char output_str[100];
    FILE *output;
    FILE *answer;

    system("cd out/production/과제2");
    
    strcpy(command, "java MainBst public/1000words.txt public/2000words.txt");
    output = popen(command, "r");
    answer = fopen("public/cpu-i5/1000words.out","r");
    
    while(!feof(answer)){
        fgets(answer_str, 100, answer);
        fgets(output_str, 100, output);
        if(!strcmp(answer_str, output_str))
            printf("%s%s\n",output_str, answer_str);
    }

    fclose(answer);
    fclose(output);

    return 0;
}
