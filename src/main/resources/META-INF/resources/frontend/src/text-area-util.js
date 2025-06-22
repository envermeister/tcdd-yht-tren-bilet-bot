window.scrollTextAreaToEnd = function(textAreaId) {
    // Increased timeout to ensure DOM is ready
    setTimeout(() => {
        const textArea = document.getElementById(textAreaId);
        if (textArea) {
            // Try to find the actual textarea element - Vaadin components wrap the native elements
            const innerTextArea = textArea.shadowRoot 
                ? textArea.shadowRoot.querySelector('textarea') 
                : textArea.querySelector('textarea');
                
            if (innerTextArea) {
                innerTextArea.scrollTop = innerTextArea.scrollHeight;
            } else if (textArea.scrollHeight) {
                // Fallback to the main element if inner textarea not found
                textArea.scrollTop = textArea.scrollHeight;
            }
            
            // Additional fallback for Vaadin's custom elements
            const customTextArea = textArea.querySelector('.v-textarea');
            if (customTextArea && customTextArea.scrollHeight) {
                customTextArea.scrollTop = customTextArea.scrollHeight;
            }
        }
    }, 100); // Increased from 50ms to 100ms for better reliability
}
