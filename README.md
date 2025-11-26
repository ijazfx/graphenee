# Graphenee
Graphenee - Enterprise Application Development Framework

![Maven Central](https://img.shields.io/maven-central/v/com.trigsoft.graphenee/gx?strategy=highestVersion
)

Graphenee word itself is a combination of Graphene and Enterprise Edition (ee). It is developed using Java, Spring Boot and Vaadin.

The motivation behind this framework is to give developers an easy to use api to quickly build enterprise applications by focusing on business logic.

Development of the Graphenee was started in April 2016 in a private bitbucket repository. On May 10, I decided to put it on Github public repository for the opensource community.

I believe the framework has a lot of potential and there is huge room for improvement. I welcome the opensource community to review and improve the code, develop and contribute modules and make Graphenee a successful for enterprise application development.

# Modules
Graphenee consists of following modules:
* gx-util - Common and utility classes, such as JPA utilities, split and run tasks in sequence/parallel, calender/datetime functions.
* gx-core - Core API, interfaces and utility methods, I18n module that is used to store and manage translation terms, Security module for creating and managing users, groups and policies, Easy access to SMS service providers such as AWS SMS, Twilio, Eocean, etc.
* gx-flow - Collection of abstract classes and reusable components to build UI based on Vaadin Flow
* gx-core-flow - UI components for gx-core modules such as UI for security users, groups and policies, i18n translation management and more. 
* gx-jbpm-embedded - Embedded jBPM engine support
* gx-jbpm-flow - UI components for gx-jbpm-embedded module such as listing processes and approval forms.
* gx-blockchain - Basic functionality to interact with Hyperledger Sawtooth and Ethereum Blockchain network.

Copyright &copy; Farrukh Ijaz, all rights reserved.

Join the team and contribute to make Graphenee a better product.

Discord Invite: https://discord.gg/kZCKGVYj
