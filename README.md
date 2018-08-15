# Example 1: Java Service Integration

Repository: [eg-01-java-jwt](https://github.com/docusign/eg-01-java-jwt)

<!--
## Articles and Screencasts

* Guide: Using OAuth JWT flow with DocuSign.
* Screencast: Using OAuth JWT flow with DocuSign.
* Guide: Sending an envelope with the Node.JS SDK.
* Screencast: Sending an example with Node.JS SDK.
-->

## Introduction

This software is an example of a **System Integration**.
This type of application interacts with DocuSign on its
own. There is no user interface and no user is present
during normal operation.

The application uses the OAuth JWT grant flow to impersonate
a user in the account.

This launcher example includes two examples:
1. Send an html, Word, and PDF file in an envelope to be signed.
1. List the envelopes in the account that are less than 30 days old.

## Installation

Requirements: Java v1.7, v1.8 or later

### Short installation instructions
* Download or clone this repository.
* The project includes a Maven pom file.
* Configure the project by copying
  `eg-01-java-jwt/src/main/resources/config-example.properties`
  to `config.properties` in the same directory and then
  updating the file. See the Configuration section, below,
  for more information.
* The project's main class is
  `com.docusign.example.jwt.JWTExample`

### Spurious warning message
When run, the application will show a spurious
warning message of `Could not reconstruct the public key`.
This message should be ignored. A future version of the
SDK will solve this issue.

### IntelliJ installation

See the [IntelliJ instructions]().

## Configure the example

You can configure the example either via a properties file or via
environment variables:

*  **config.properties:** In the **src/main/resources/**
   directory, copy the
   `config-example.properties` file  to `config.properties`.

   Then update it with your settings.
   More information for the configuration settings is below.
*  Or via **environment variables:** export the needed
   environment variables.
   The variable names in the `config-example.properties` file
   are the same as the needed environment variables.

`config.properties` is in the .gitignore file so your
private information will not be added to your repository.
Do not store your Integration Key, private key, or other
private information in your code repository.

#### Creating the Integration Key
Your DocuSign Integration Key must be configured for a JWT OAuth authentication flow:
* Using the DocuSign Admin tool,
  create a public/private key pair for the integration key.
  Store the private key
  in a secure location. You can use a file or a key vault.
* The example requires the private key. Store the private key in the
  `config.properties` file or in the environment variable
  `DS_PRIVATE_KEY`.
* Due to Java handling of multi-line strings, add the
  text `\n\` at the end of each line of the private key.
  See the example in the `config-example.properties` file.
* If you will be using individual consent grants, you must create a
  `Redirect URI` for the key. Any URL can be used. By default, this
  example uses `https://www.docusign.com`

#### The impersonated user's guid
The JWT will impersonate a user within your account. The user can be
an individual or a user representing a group such as "HR".

The example needs the guid assigned to the user.
The guid value for each user in your account is available from
the DocuSign Admin tool in the **Users** section.

To see a user's guid, **Edit** the user's information.
On the **Edit User** screen, the guid for the user is shown as
the `API Username`.

## Run the examples

The project's main class is `com.docusign.example.jwt.JWTExample`

## Support, Contributions, License

Submit support questions to [StackOverflow](https://stackoverflow.com). Use tag `docusignapi`.

Contributions via Pull Requests are appreciated.
All contributions must use the MIT License.

This repository uses the MIT license, see the
[LICENSE](https://github.com/docusign/eg-01-java-jwt/blob/master/LICENSE) file.
