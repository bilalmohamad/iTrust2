#Author msmahate
Feature: Calculate R-Naught
	As a Virologist
	I want to calculate R-Naught
	So that I can see how fast the virus spreads

Scenario: Upload Passengers
    When I log into iTrust2 as a virologist
    When I navigate to the Upload Passenger List page
    When I upload a file 
    Then It displays the number of passenger entries made
    
Scenario: Calculate Rnaught
    When I log into iTrust2 as a virologist
    When I navigate to the Upload Passenger List page
    When I upload a file 
    Then It displays the number of passenger entries made
    Then I navigate to the Calculate R0 page
    When I click Calculate
    Then it displays the calculation