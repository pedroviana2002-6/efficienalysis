# Efficienalysis

Aplicativo desktop (Java + Swing) para acompanhar a **eficiência ambiental de uma empresa mês a mês**.
A cada mês o usuário registra o consumo de energia, as emissões de gases de efeito estufa e os materiais
não biodegradáveis utilizados pela empresa; o aplicativo compara dois meses e mostra, em porcentagem, quanto a empresa ganhou ou
perdeu de eficiência em cada indicador.

> Projeto temático do curso de Análise e Desenvolvimento de Sistemas da Universidade de Caxias do Sul (UCS) 2025, projeto editado em 2026. O planejamento original
> (requisitos, casos de uso, diagramas) está em ["docs/pre-projeto.pdf"](docs/pre-projeto.pdf).

## Funcionalidades

Criação de conta e login (senha protegida com hash PBKDF2 + salt), 
registro de energia (kWh), emissões (tCO2) e materiais não biodegradáveis (kg),
armazenamento e visualização de relatórios mensais, modificação de um relatório já registrado,
comparação entre dois meses (variação % por indicador),
relatório da comparação, exportável em .txt

Como os três indicadores são de consumo/emissão/resíduo, **variação negativa = eficiência ganha**.

## Como executar

Requisitos: **JDK 21+** e **Maven 3.9+**.

```bash
mvn clean package
java -jar target/efficienalysis-1.0.0.jar
```

Também é possível abrir a pasta numa IDE (Eclipse, IntelliJ, VS Code) como *projeto Maven* e executar
a classe `br.sistema.applauncher.AppLauncher`.

## Onde ficam os dados

Os dados são salvos em JSON na pasta `~/.efficienalysis/` (`usuarios.json` e `relatorios.json`).
Se um arquivo estiver corrompido, o aplicativo guarda uma cópia como `*.corrompido` em vez de sobrescrevê-lo.

## Estrutura

Arquitetura em camadas: apresentação (userinterface), regras e dados, e persistência em JSON (Jackson).

## Diferenças em relação ao pré-projeto

- O login usa **usuário e senha** ao invés de um ID gerado pelo sistema.
- Funções com os relatórios mensais em **CSV** ainda não foram implementadas.
- Além disso, novas classes foram implementadas para resolver problemas nas versões anteriores do programa.

## Limitações conhecidas / próximos passos

- Os relatórios são compartilhados por todas as contas do mesmo computador (não há relatórios por usuário).
- Não há testes automatizados ainda.
- Importação/exportação em CSV.
- Falta implementação de regras de negócio e requisitos não funcionais, não indicadas no planejamento original.
