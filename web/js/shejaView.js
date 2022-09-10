$(function ()
{
    // -- books list -- //

    function setULStatus($ul) {
        var omniTrue = true, omniFalse = false;
        $ul.find('div > div > input:checkbox').each(function () {
            var b = $(this).prop('checked');
            omniTrue &= b;
            omniFalse |= b;
        });
        var $menu = $ul.children('input:checkbox').first();

        $menu.prop('indeterminate', false);
        if ( omniTrue ) $menu.prop('checked', true);
        else if ( !omniFalse ) $menu.prop('checked', false);
        else $menu.prop('indeterminate', true);
    }

    function init() {
        $('div.book-list > ul > div')
            .hide()
            .addClass('bg-dark text-white')
            .css('position', 'absolute')
            .css('padding', 10)
            .css('overflow', 'hidden');

        $('div.book-list > ul').each(function () {
            setULStatus( $(this) );
        });
    }

    $('div.book-list > ul > input:checkbox').click(function() {
        $(this).siblings('div').first().find('input:checkbox').prop('checked', ($(this).is(':checked')));
    });

    $('div.book-list > ul > div > div > input:checkbox').click(function() {
        setULStatus( $(this).closest('ul') );
    });

    $('div.book-list > ul a').click(function (e) {
        e.preventDefault();
        $d = $(this).closest('ul').children('div').first();
        if( $d.css('display') === "block" ) {
            $d.css("z-index", 1 );
        } else {
            $d.css("z-index", 1000 );
        }
        $d.toggle("fast");
    });

    $('div.book-list > ul').mouseleave(function () {
        $d = $(this).children('div').first();
        if( $d.css('display') === "block" ) {
            $d.toggle("fast").css("z-index", 1 );
        }
    });

    init();

    // -- search form -- //

    $('#wholeWord').click(function () {
        $('#wylie').prop("disabled", !($(this).prop("checked")));
    });

    $('form').on('submit', function (e) {
        var search = $("input[name='q']");
        var t = search.val().trim();
        var books = $('.book-list').find('input:checked');
        if (t.length === 0 || books.length === 0) {
            e.preventDefault();
        }
    });

    // -- appendices table -- //

    var sort_order = [];

    function table_init()
    {
        var ths = $('table.table > thead > tr > th');
        ths.css( 'cursor', 'pointer' );
        sort_order[0] = 'asc';
        for(var i = 1; i < ths.length; i++) sort_order[i] = 'no';
    }

    $('table.table > thead > tr > th').click(function ()
    {
        var idx = $(this).index();
        if (idx === 1) idx = 0;

        var t = sort_order[idx];
        var ths = $(this).closest('tr').children();
        for(var i = 0; i < ths.length; i++) sort_order[i] = 'no';
        sort_order[idx] = ( t === 'asc') ? 'desc' : 'asc';

        var $table = $(this).closest('table');
        $table.addClass("while-progress-bg");
        $p = $(".while-progress");
        $p.find('label').text( $(this).text() + " sorting (" + sort_order[idx] + ")");
        $p.removeClass("d-none");

        setTimeout(function() {
            sortTable( $table, idx, sort_order[idx] );
            $p.addClass("d-none");
            $table.removeClass("while-progress-bg");
        }, 0);
    });

    function sortTable(table, column, order) {
        var asc   = order === 'asc';
        var tbody = table.find('tbody');

        var s = 'td:eq(' + column  + ')';

        tbody
            .find('tr')
            .sort( function(a, b) {
                if (asc) {
                    return $(s, a).text().localeCompare($(s, b).text());
                } else {
                    return $(s, b).text().localeCompare($(s, a).text());
                }
            })
            .each( function() {
                $(this).removeClass('d-none');
                if ( $(s, this).text().trim() === "" ) {
                    $(this).addClass('d-none');
                }
            })
            .appendTo(tbody);
    }

    table_init();

    // -- nav bar appendices ajax -- //

    $('.nav-link').click(function (e) {
        e.preventDefault();

        if ($(this).attr('class').indexOf('disabled') !== -1) return false;

        table_init();

        $('div.container').addClass("while-progress-bg");
        $p = $(".while-progress");
        $p.find('label').text( "Loading...");
        $p.removeClass("d-none");

        var t = $(this).text().trim().toLowerCase();
        setTimeout(function() {
            provideRows(t);
            $p.addClass("d-none");
            $('div.container').removeClass("while-progress-bg");
        }, 0);
    });

});

