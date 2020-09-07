import React, { createContext, useState } from 'react';

export const BrowserContext = createContext();

// Browser context object used to determine the browser for rendering supported
// DOM elements

const BrowserContextProvider = (props) => {
  const [isFirefox] = useState(typeof InstallTrigger !== 'undefined');
  const [isChrome] = useState(!!window.chrome);

  return (
    <BrowserContext.Provider value={{ isFirefox, isChrome }}>
      {props.children}
    </BrowserContext.Provider>
  );
};

export default BrowserContextProvider;
