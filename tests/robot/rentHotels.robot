*** Settings ***
Library     SeleniumLibrary
Resource    ./rentHotels.resource

*** Test Cases ***
Processo para efetuar cadastro no site RoyalReservations
    Abrir o site RoyalReservations I
    Clicar no botão Criar Conta
    Inserir dados no Formulário de Cadastro
    Concluir cadastro

Processo para fazer login no site RoyalReservations
    Abrir o site RoyalReservations II
    Clicar no botão de Iniciar sessão
    Inserir dados no Formulário de Login
    Concluir Login

Processo para efetuar uma reserva partindo de uma navegação anônima
    Abrir o site RoyalReservations III
    Realizar uma busca
    Selecionar produto
    Selecionar uma data e iniciar reserva
    Efetuar o login
    Completar dados do formulário de reserva
    Selecionar horário de checkin    
    Confirmar reserva

# Processo para efetuar logoff
#     Abrir o site RoyalReservations IV
#     Efetuar o login
#     Efetuar o logoff

