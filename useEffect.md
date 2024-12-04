When you call useEffect(), you're telling React to run your "effect" function after flushing changes to the DOM, which means, when the component (some page) mounts (renders/ builds), it needs to fetch data (e.g., channels, messages) immediately without user interaction.
**useEffect** runs after the component renders, making it ideal for initiating data fetching on mount.

- Different from actions like links or buttons since the action is triggered by an event (button click), we define the function and call it in response to the event handler.

- useEffect is used when you need to perform an action upon component mount or when certain dependencies change.

```

useEffect(() => {
    const fetchChannels = async () => {
      if (authData) {
        try {
          const data = await getUserChannels(authData.token);
          setChannels(data);
        } catch (err) {
          setError('Failed to load channels.');
        }
      }
    };
    fetchChannels();
  }, [authData]);

  ```

- Depends upon authData change
- If [] is empty depends on mount, just one call
