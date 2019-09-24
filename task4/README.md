# Task 4 - Schemes

## Prompt

While analyzing the TerrorTime SQLite database found on the terrorist’s device, analysts discovered that the database has cached credentials but requires a pin to log in. If we can determine how the credentials are protected and find a way to recover the pin, we should be able to masquerade as the arrested terrorist. Perform reverse engineering to identify how the terrorist’s credentials are protected and submit the terrorist's Client ID and Client Secret. Once you have uncovered their credentials, masquerade (i.e., login) as him using the TerrorTime app. Review their chat history and assess additional information regarding their organization. Though the app encrypts messages back and forth, the terrorists have previously spoken in some form of code to ensure their plans were protected. To prove completion of this task, you will need to analyze the terrorist's conversation history in order to uncover/deduce the following information:

1. Terror Cell Leader's Username
2. Action planned by the terrorist cell
3. The date on which the action will occur

## Solution

### Reversing the PIN Process
