import React, { createContext, Component } from 'react';

export const BrowserContext = createContext();

// Browser context object used to determine the browser for rendering supported
// DOM elements

class BrowserContextProvider extends Component {
  state = {
    isFirefox: typeof InstallTrigger !== 'undefined',
    isChrome: !!window.chrome,
  };
  render() {
    return (
      <BrowserContext.Provider value={{ ...this.state }}>
        {this.props.children}
      </BrowserContext.Provider>
    );
  }
}

export default BrowserContextProvider;
