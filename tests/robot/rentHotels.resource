*** Settings ***
Library    SeleniumLibrary
*** Variables ***
${BROWSER}                         Edge
${SITE}                            http://royalreservations.ctdprojetointegrador.com/

##USER DATA               
${USER_EMAIL}                      robot@test.com
${USER_PASSWORD}                   robottest
${USERNAME}                        Robot
${USER_SURNAME}                    Test
${CATEGORIES_CONTAINER}            xpath=/html/body/div/div[2]/main/div/div[2]      

# PARA FAZER CADASTRO 
${SIGN_IN_BUTTON}                  xpath=/html/body/div/div[2]/header/div[2]/nav/ul/li[1]/button
${USERNAME_INPUT}                  xpath=/html/body/div/div[2]/main/div/form/div[2]/div[1]/div/div/div/input
${USER_SURNAME_INPUT}              xpath=/html/body/div/div[2]/main/div/form/div[2]/div[2]/div/div/div/input
${REGISTER_EMAIL_INPUT}            xpath=/html/body/div/div[2]/main/div/form/div[3]/div/div/div/input
${REPEAT_EMAIL_INPUT}              xpath=/html/body/div/div[2]/main/div/form/div[4]/div/div/div/input
${REGISTER_PASSWORD}               xpath=/html/body/div/div[2]/main/div/form/div[5]/div/input
${SIGN_IN_FORM_BUTTON}             xpath=/html/body/div/div[2]/main/div/form/button

##PARA FAZER LOGIN
${LOGIN_BUTTON}                    xpath=/html/body/div/div[2]/header/div[2]/nav/ul/li[2]/button
${USER_LOGIN_INPUT}                id=outlined-basic
${USER_PASSWORD_INPUT}             id=outlined-adornment-password
${LOGIN_FORM_BUTTON}               xpath=/html/body/div/div[2]/main/div/form/button

## PARA EFETUAR RESERVA  
${SELECTED_CITY}   xpath=/html/body/div/div[2]/main/div/div[1]/form/div[1]/select/option[2]
${CITY_SEARCH_SELECT}    xpath=/html/body/div/div[2]/main/div/div[1]/form/div[1]/select
${DATE_SEARCH_INPUT}    ##input para inserir data que quer buscar
${SEARCH_BUTTON}        xpath=/html/body/div/div[2]/main/div/div[1]/form/button
${DETAILS_SEARCHED_HOTEL_BUTTON}    xpath=/html/body/div/div[2]/main/div/div[3]/div[1]/li/div/div[2]/button
${AVAILABLE_DATES}    xpath=/html/body/div/div[2]/main/div/div[7]/div[1]/div[2]/div/div/div
${START_BOOKING_DATE}    xpath=/html/body/div/div[2]/main/div/div[7]/div[1]/div[2]/div/div/div/div[2]/div[1]/div[2]/div[4]/div[3]/span
${END_BOOKING_DATE}    xpath=/html/body/div/div[2]/main/div/div[7]/div[1]/div[2]/div/div/div/div[2]/div[1]/div[2]/div[4]/div[6]/span
${START_BOOKING}    xpath=/html/body/div/div[2]/main/div/div[7]/div[2]/form/button
${ORIGIN_CITY_INPUT}    xpath=/html/body/div/div[2]/main/div/div[1]/div[2]/div[1]/div[1]/div[4]/div/div/div/div/input
${ORIGIN_CITY}    Florianópolis
${ARRIVAL_TIME_CONTAINER}    xpath=/html/body/div/div[2]/main/div/div[1]/div[2]/div[1]/div[3]/div
${SELECT_CHECKIN_TIME}    xpath=/html/body/div/div[2]/main/div/div[1]/div[2]/div[1]/div[3]/div/div[3]/div/span
${SELECTED_TIME}        id=:rl:
${CONFIRM_BOOKING_BUTTON}    xpath=/html/body/div/div[2]/main/div/div[1]/div[2]/div[2]/div/button

*** Keywords ***

########### ABRIR SITE E EFETUAR CADASTRO ###########
Abrir o site RoyalReservations I
    Open Browser                     ${SITE}                            ${BROWSER}
    Maximize Browser Window
    Wait Until Element Is Visible    ${CATEGORIES_CONTAINER}

Clicar no botão Criar Conta
    Click Element                    ${SIGN_IN_BUTTON}

Inserir dados no Formulário de Cadastro
    Input Text                       ${USERNAME_INPUT}                  ${USERNAME}  
    Input Text                       ${USER_SURNAME_INPUT}              ${USER_SURNAME}
    Input Text                       ${REGISTER_EMAIL_INPUT}            ${USER_EMAIL}
    Input Text                       ${REPEAT_EMAIL_INPUT}              ${USER_EMAIL}
    Input Text                       ${REGISTER_PASSWORD}               ${USER_PASSWORD}

Concluir cadastro
    Click Element                    ${SIGN_IN_FORM_BUTTON}

########## ABRIR SITE E EFETUAR LOGIN ###########
Abrir o site RoyalReservations II
    Open Browser                     ${SITE}                            ${BROWSER}
    Maximize Browser Window
    Wait Until Element Is Visible    ${CATEGORIES_CONTAINER}

Clicar no botão de Iniciar sessão
    Click Element                    ${LOGIN_BUTTON}

Inserir dados no Formulário de Login
    Input Text                       ${USER_LOGIN_INPUT}                ${USER_EMAIL}
    Input Text                       ${USER_PASSWORD_INPUT}             ${USER_PASSWORD}

Concluir Login
    Click Element                    ${LOGIN_FORM_BUTTON}
    Wait Until Element Is Visible    ${CATEGORIES_CONTAINER}

########### ABRIR O SITE E EFETUAR UMA RESERVA ###########
Abrir o site RoyalReservations III
    Open Browser                     ${SITE}                            ${BROWSER}
    Maximize Browser Window
    Wait Until Element Is Visible    ${CATEGORIES_CONTAINER}

Realizar uma busca
    Click Element    ${CITY_SEARCH_SELECT}
    Sleep    2s
    Click Element    ${SELECTED_CITY}
    Click Element    ${SEARCH_BUTTON}

Selecionar produto
    Scroll Element Into View    xpath=/html/body/div/div[2]/main/div/div[3]/div[1]/li/div/div[2]
    Sleep    1s
    Click Element    ${DETAILS_SEARCHED_HOTEL_BUTTON}

Selecionar uma data e iniciar reserva
    Scroll Element Into View    ${AVAILABLE_DATES}
    Click Element    ${START_BOOKING_DATE}
    Click Element    ${END_BOOKING_DATE}
    Click Element    ${START_BOOKING}

Efetuar o login
    Input Text                       ${USER_LOGIN_INPUT}                ${USER_EMAIL}
    Input Text                       ${USER_PASSWORD_INPUT}             ${USER_PASSWORD}
    Click Element                    ${LOGIN_BUTTON}

Completar dados do formulário de reserva
    Input Text    ${ORIGIN_CITY_INPUT}    ${ORIGIN_CITY}

Selecionar horário de checkin    
    Scroll Element Into View    ${ARRIVAL_TIME_CONTAINER}
    Click Element    ${SELECT_CHECKIN_TIME}
    Sleep    2s
    Click Element    ${SELECTED_TIME}

Confirmar reserva
    Click Element    ${CONFIRM_BOOKING_BUTTON}