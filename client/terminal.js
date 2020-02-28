var util = util || {};
util.toArray = function(list) {
  return Array.prototype.slice.call(list || [], 0);
};

var Terminal = Terminal || function(cmdLineContainer, outputContainer) {
  window.URL = window.URL || window.webkitURL;
  window.requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem;

  var cmdLine_ = document.querySelector(cmdLineContainer);
  var output_ = document.querySelector(outputContainer);

  const CMDS_ = [
    'all expenses', 'all expenses THIS WEEK/MONTH/YEAR', '50 spaeti bar/paypal/card', 'statistic THIS WEEK/MONTH/YEAR', 'add category NAME GOAL', 'update category NAME GOAL', 'delete category NAME', 'all categories', 'help'
  ];
  
  var fs_ = null;
  var cwd_ = null;
  var history_ = [];
  var histpos_ = 0;
  var histtemp_ = 0;

  window.addEventListener('click', function(e) {
    cmdLine_.focus();
  }, false);

  cmdLine_.addEventListener('click', inputTextClick_, false);
  cmdLine_.addEventListener('keydown', historyHandler_, false);
  cmdLine_.addEventListener('keydown', processNewCommand_, false);

  //
  function inputTextClick_(e) {
    this.value = this.value;
  }

  //
  function historyHandler_(e) {
    if (history_.length) {
      if (e.keyCode == 38 || e.keyCode == 40) {
        if (history_[histpos_]) {
          history_[histpos_] = this.value;
        } else {
          histtemp_ = this.value;
        }
      }

      if (e.keyCode == 38) { // up
        histpos_--;
        if (histpos_ < 0) {
          histpos_ = 0;
        }
      } else if (e.keyCode == 40) { // down
        histpos_++;
        if (histpos_ > history_.length) {
          histpos_ = history_.length;
        }
      }

      if (e.keyCode == 38 || e.keyCode == 40) {
        this.value = history_[histpos_] ? history_[histpos_] : histtemp_;
        this.value = this.value; // Sets cursor to end of input.
      }
    }
  }

  //
  function processNewCommand_(e) {

    if (e.keyCode == 9) { // tab
      e.preventDefault();
      // Implement tab suggest.
    } else if (e.keyCode == 13) { // enter
      // Save shell history.
      if (this.value) {
        history_[history_.length] = this.value;
        histpos_ = history_.length;
      }

      // Duplicate current input and append to output section.
      var line = this.parentNode.parentNode.cloneNode(true);
      line.removeAttribute('id')
      line.classList.add('line');
      var input = line.querySelector('input.cmdline');
      input.autofocus = false;
      input.readOnly = true;
      output_.appendChild(line);

      if (this.value && this.value.trim()) {
        var args = this.value.split(' ').filter(function(val, i) {
          return val;
        });
      
        var cmd = args[0].toLowerCase();
        
        args = args.splice(1); // Remove cmd from arg list.
      }

    //******CATEGORY*****
    //add Category DONE
    //update Category DONE
    //delete Category DONE
    //all Categories DONE

    //****EXPENSE******
    //add Expense
    //get all Expensen //all expenses
    //Get all Expenses in Timespan //all expenses this week/last week/month/year
    //Get expense Statitic //statistic this week/ last week/month/year
    var url = 'http://localhost:8081';
      if(cmd && cmd == 'all' && args[0] && args[0] == "expenses") {
        if(!args[1]) {
          $.ajax( {
            url: url + '/expense/allExpenses',
            type:"GET",
            headers: {
              "Access-Control-Allow-Header":"x-requested-with",
              "Content-Type":"application/json"
            }, 
            success:function(data, textStatus, jqXHR) {
              var outputString = "All Expenses: <br>";
              for(var i = 0; i < data.length; i++) {
                var category = data[i].categoryWrapper.name;
                var amount = data[i].value;
                var timestamp = data[i].timestamp;
                var wayMoneySpent = data[i].wayMoneySpent;
                var outputString = outputString + "category: " + category + " amount : " + amount + " timestamp: " + timestamp + " way money spend: " + wayMoneySpent + "<br>";
              }
              output(outputString);
            },
            error: function(jqXHR, textStatus, errorThrown) {
              output('Ops... something went wrong.');
            }
          });
          cmd = null;
        }

        if(args[1] && args[1] == "this") {
          if(args[2]) {
            switch(args[2]) {
              case 'week': 
                var monday = getMonday(new Date());
                var today = new Date();
                $.ajax( {
                  url: url + '/expense?from=' + monday.toISOString() + '&till=' + today.toISOString(),
                  type:"GET",
                  headers: {
                    "Access-Control-Allow-Header":"x-requested-with",
                    "Content-Type":"application/json"
                  }, 
                  success:function(data, textStatus, jqXHR) {
                    var outputString = "All Expenses this week: <br>";
                    for(var i = 0; i < data.length; i++) {
                      var category = data[i].categoryWrapper.name;
                      var amount = data[i].value;
                      var timestamp = data[i].timestamp;
                      var wayMoneySpent = data[i].wayMoneySpent;
                      var outputString = outputString + "category: " + category + " amount : " + amount + " timestamp: " + timestamp + " way money spend: " + wayMoneySpent + "<br>";
                    }
                    output(outputString);
                  },
                  error: function(jqXHR, textStatus, errorThrown) {
                    output('Ops... something went wrong.');
                  }
                });
                cmd = null;
                break;
              case 'month': 
                var date = new Date();
                var firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1);
                var today = new Date();
                $.ajax( {
                  url: url + '/expense?from=' + firstDayOfMonth.toISOString() + '&till=' + today.toISOString(),
                  type:"GET",
                  headers: {
                    "Access-Control-Allow-Header":"x-requested-with",
                    "Content-Type":"application/json"
                  }, 
                  success:function(data, textStatus, jqXHR) {
                    var outputString = "All Expenses this month: <br>";
                    for(var i = 0; i < data.length; i++) {
                      var category = data[i].categoryWrapper.name;
                      var amount = data[i].value;
                      var timestamp = data[i].timestamp;
                      var wayMoneySpent = data[i].wayMoneySpent;
                      var outputString = outputString + "category: " + category + " amount : " + amount + " timestamp: " + timestamp + " way money spend: " + wayMoneySpent + "<br>";
                    }
                    output(outputString);
                  },
                  error: function(jqXHR, textStatus, errorThrown) {
                    output('Ops... something went wrong.');
                  }
                });
                cmd = null;
                break;
              case 'year': 
                var firstDayOfYear = new Date(new Date().getFullYear(), 0, 1);
                var today = new Date();
                $.ajax( {
                  url: url + '1/expense?from=' + firstDayOfYear.toISOString() + '&till=' + today.toISOString(),
                  type:"GET",
                  headers: {
                    "Access-Control-Allow-Header":"x-requested-with",
                    "Content-Type":"application/json"
                  }, 
                  success:function(data, textStatus, jqXHR) {
                    var outputString = "All Expenses this year: <br>";
                    for(var i = 0; i < data.length; i++) {
                      var category = data[i].categoryWrapper.name;
                      var amount = data[i].value;
                      var timestamp = data[i].timestamp;
                      var wayMoneySpent = data[i].wayMoneySpent;
                      var outputString = outputString + "category: " + category + " amount : " + amount + " timestamp: " + timestamp + " way money spend: " + wayMoneySpent + "<br>";
                    }
                    output(outputString);
                  },
                  error: function(jqXHR, textStatus, errorThrown) {
                    output('Ops... something went wrong.');
                  }
                });
                cmd = null;
                break;
              default: //no return
            }
          }
        }
        cmd = null;
      }
      //50 rewe bar
      if(!isNaN(cmd) && cmd) {
        if(args[0] && args[1]) {
          if(args[1] == "bar") {
            args[1] = "CASH";
          } else if(args[1] == "card") {
            args[1] = "CARD";
          } else if(args[1] == "paypal") {
            args[1] = "PAYPAL";
          }

          $.ajax( {
            url: url + '/expense',
            type:"POST",
            headers: {
              "Access-Control-Allow-Header":"x-requested-with",
              "Content-Type":"application/json"
            },
            data:JSON.stringify({
              "value": cmd,
              "category": args[0],
              "wayMoneySpent": args[1]
            }), 
            success:function(data, textStatus, jqXHR) {
              output('Added your money');
            },
            error: function(jqXHR, textStatus, errorThrown) {
              output('Ops... something went wrong. ' + jqXHR.responseText);
            }
          });
        }
        cmd = null;
      }

      console.info(cmd);
      switch (cmd) {
        //statistic this week
        case 'statistic':
          if(args[0] && args[0] == "this") {
            if(args[1]) {
              switch(args[1]) {
                case 'week': 
                  var monday = getMonday(new Date());
                  var today = new Date();
                  $.ajax( {
                    url: url + '/expense/statistic?from=' + monday.toISOString() + '&till=' + today.toISOString(),
                    type:"GET",
                    headers: {
                      "Access-Control-Allow-Header":"x-requested-with",
                      "Content-Type":"application/json"
                    }, 
                    success:function(data, textStatus, jqXHR) {
                      output(JSON.stringify(data));
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                      output('Ops... something went wrong.');
                    }
                  });
                  cmd = null;
                  break;
                case 'month': 
                  var date = new Date();
                  var firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1);
                  var today = new Date();
                  $.ajax( {
                    url: url + '/expense/statistic?from=' + firstDayOfMonth.toISOString() + '&till=' + today.toISOString(),
                    type:"GET",
                    headers: {
                      "Access-Control-Allow-Header":"x-requested-with",
                      "Content-Type":"application/json"
                    }, 
                    success:function(data, textStatus, jqXHR) {
                      output(JSON.stringify(data));
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                      output('Ops... something went wrong.');
                    }
                  });
                  cmd = null;
                  break;
                case 'year': 
                  var firstDayOfYear = new Date(new Date().getFullYear(), 0, 1);
                  var today = new Date();
                  $.ajax( {
                    url: url + '/expense/statistic?from=' + firstDayOfYear.toISOString() + '&till=' + today.toISOString(),
                    type:"GET",
                    headers: {
                      "Access-Control-Allow-Header":"x-requested-with",
                      "Content-Type":"application/json"
                    }, 
                    success:function(data, textStatus, jqXHR) {
                      output(JSON.stringify(data));
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                      output('Ops... something went wrong.');
                    }
                  });
                  cmd = null;
                  break;
                default: //no return
              }
            }
          }
          cmd = null;
          break;
        //add category hans 50
        case 'add': 
        if(args[0].toLowerCase() == 'category') {
          if(args[1].toLowerCase() && args[2]) {

            $.ajax( {
              url:url + '/category',
              type:"POST",
              headers: {
                "Access-Control-Allow-Header":"x-requested-with",
                "Content-Type":"application/json"
              },
              data:JSON.stringify({"name": args[1], "goal": args[2]}), 
              success:function(data, textStatus, jqXHR) {
                output('Category ' + args[1] + " was added successfully with goal " + args[2]);
              },
              error: function(jqXHR, textStatus, errorThrown) {
                output('Ops... something went wrong. Category not saved');
              }
            });
          }
        }
        break;

        //update category hans 100
        case 'update': 
        if(args[0].toLowerCase() == 'category') {
          if(args[1].toLowerCase() && args[2]) {

            $.ajax( {
              url:url + '/category',
              type:"PUT",
              headers: {
                "Access-Control-Allow-Header":"x-requested-with",
                "Content-Type":"application/json"
              },
              data:JSON.stringify({"name": args[1], "goal": args[2]}), 
              success:function(data, textStatus, jqXHR) {
                output('Category ' + args[1] + " was updated successfully with goal " + args[2]);
              },
              error: function(jqXHR, textStatus, errorThrown) {
                output('Ops... something went wrong. ' + jqXHR.responseText);
              }
            });
          }
        }
        break;

        //delete category hans
        case 'delete': 
        if(args[0].toLowerCase() == 'category') {
          if(args[1].toLowerCase()) {

            $.ajax( {
              url:url + '/category/' + args[1],
              type:"DELETE",
              headers: {
                "Access-Control-Allow-Header":"x-requested-with",
                "Content-Type":"application/json"
              },
              success:function(data, textStatus, jqXHR) {
                output('Category ' + args[1] + ' was deleted successfully');
              },
              error: function(jqXHR, textStatus, errorThrown) {
                output('Ops... something went wrong. ' + jqXHR.responseText);
              }
            });
          }
        }
        break;

        //all categories
        case 'all': 
        if(args[0].toLowerCase() == 'categories') {
          $.ajax( {
            url:url + '/category/allCategories',
            type:"GET",
            headers: {
              "Access-Control-Allow-Header":"x-requested-with",
              "Content-Type":"application/json"
            }, 
            success:function(data, textStatus, jqXHR) {
              var outputString = "";
              for(var i = 0; i < data.length; i++) {
                outputString = outputString + "</br>" + data[i].name + " with goal " + data[i].goal;
              }
              output('Saved Categories: ' + outputString);
            },
            error: function(jqXHR, textStatus, errorThrown) {
              output('Ops... something went wrong.');
            }
          });
        }
        break;
        case 'help':
          console.log(CMDS_.join('<br>'));
          output(CMDS_.join('<br>'));
          break;
        default:
          if (cmd) {
            output(cmd + ': command not found');
          }
      };

      window.scrollTo(0, getDocHeight_());
      this.value = ''; // Clear/setup line for next input.
    }
  }

  function getMonday(d) {
    d = new Date(d);
    var day = d.getDay(),
        diff = d.getDate() - day + (day == 0 ? -6:1); // adjust when day is sunday
    return new Date(d.setDate(diff));
  }

  //
  function formatColumns_(entries) {
    var maxName = entries[0].name;
    util.toArray(entries).forEach(function(entry, i) {
      if (entry.name.length > maxName.length) {
        maxName = entry.name;
      }
    });

    var height = entries.length <= 3 ?
        'height: ' + (entries.length * 15) + 'px;' : '';

    // 12px monospace font yields ~7px screen width.
    var colWidth = maxName.length * 7;

    return ['<div class="ls-files" style="-webkit-column-width:',
            colWidth, 'px;', height, '">'];
  }

  //
  function output(html) {
    output_.insertAdjacentHTML('beforeEnd', '<p>' + html + '</p>');
  }

  // Cross-browser impl to get document's height.
  function getDocHeight_() {
    var d = document;
    return Math.max(
        Math.max(d.body.scrollHeight, d.documentElement.scrollHeight),
        Math.max(d.body.offsetHeight, d.documentElement.offsetHeight),
        Math.max(d.body.clientHeight, d.documentElement.clientHeight)
    );
  }

  //
  return {
    init: function() {
      output('<img align="left" src="http://www.w3.org/html/logo/downloads/HTML5_Badge_128.png" width="100" height="100" style="padding: 0px 10px 20px 0px"><h2 style="letter-spacing: 4px">HTML5 Web Terminal</h2><p>' + new Date() + '</p><p>Enter "help" for more information.</p>');
    },
    output: output
  }
};